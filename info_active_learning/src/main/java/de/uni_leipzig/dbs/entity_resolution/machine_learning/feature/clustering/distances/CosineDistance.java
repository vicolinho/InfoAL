package de.uni_leipzig.dbs.entity_resolution.machine_learning.feature.clustering.distances;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import weka.core.*;
import weka.core.neighboursearch.PerformanceStats;

/**
 * <!-- globalinfo-start --> Implements the Cosine distance. The distance
 * between two points is the cosine value of their corresponding vectors (from
 * the origin).<br/>
 * <br/>
 * For more information, see:<br/>
 * <br/>
 * Wikipedia. Cosine similarity. URL
 * http://en.wikipedia.org/wiki/Cosine_similarity.
 * <p/>
 * <!-- globalinfo-end -->
 *
 * <!-- technical-bibtex-start --> BibTeX:
 *
 * <pre>
 * &#064;misc{missing_id,
 *    author = {Wikipedia},
 *    title = {Cosine similarity},
 *    URL = {http://en.wikipedia.org/wiki/Cosine_similarity}
 * }
 * </pre>
 * <p/>
 * <!-- technical-bibtex-end -->
 *
 * <!-- options-start --> Valid options are:
 * <p/>
 *
 * <pre>
 * -B
 *  Turns on using binary presence and absence of attribute
 *  values instead of their frequencies in distance calculation.
 * </pre>
 *
 * <pre>
 * -R &lt;col1,col2-col4,...&gt;
 *  Specifies list of columns to used in the calculation of the
 *  distance. 'first' and 'last' are valid indices.
 *  (default: first-last)
 * </pre>
 *
 * <pre>
 * -V
 *  Invert matching sense of column indices.
 * </pre>
 *
 * <!-- options-end -->
 *
 * @author Anna Huang (lh92 at waikato dot ac dot nz)
 * @version $Revision: 5953 $
 */
public class CosineDistance implements DistanceFunction, Serializable,
        RevisionHandler {

  /** for serialization. */
  private static final long serialVersionUID = -3705163763287698995L;

  /** True if only count attribute's presence and absence */
  protected boolean m_binary;

  /** Set the range of attributes to be used */
  protected Range m_Range;

  /** True if ignore attributes in the specified range */
  protected boolean m_invertSelection;

  /** The boolean flags, whether an attribute will be used or not. */
  protected boolean[] m_ActiveIndices;

  /** the instances used internally. */
  protected Instances m_Data;

  /** Whether all the necessary preparations have been done. */
  protected boolean m_Validated;

  /**
   * Returns a string describing this object.
   *
   * @return a description of the evaluator suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String globalInfo() {
    return "The cosine rule of vector similarity. Take (1 - cosine value) as distance.";
  }

  /**
   * Returns the revision string.
   *
   * @return the revision
   */
  public String getRevision() {
    return RevisionUtils.extract("$Revision: 5953 $");
  }

  /**
   * Gets the current settings. Returns empty array.
   *
   * @return an array of strings suitable for passing to setOptions()
   */
  @Override
  public String[] getOptions() {
    String[] options = new String[4];
    if (getBinary()) {
      options[0] = "-B";
    } else {
      options[0] = "";
    }
    if (getInvertSelection()) {
      options[1] = "-V";
    } else {
      options[1] = "";
    }
    options[2] = "-R";
    options[3] = getAttributeIndices();

    return options;
  }

  /**
   * Returns an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  @Override
  public Enumeration listOptions() {
    Vector newVector = new Vector();
    newVector.addElement(new Option(
            "\tSets whehter count attribute's weight or \n"
                    + "\tits absence or occurrence (binary mode).", "B", 0, "-B"));

    newVector.addElement(new Option(
            "\tSets whether the matching sense of attribute \n"
                    + "\tindices is inverted or not.", "V", 0, "-V"));

    newVector.addElement(new Option("\tSet the range of attributes to be used",
            "R", 1, "-R"));
    return newVector.elements();
  }

  /**
   * Parses a given list of options.
   *
   * @param options
   *          the list of options as an array of strings
   * @throws Exception
   *           if an option is not supported
   */
  @Override
  public void setOptions(String[] options) throws Exception {
    setBinary(Utils.getFlag('B', options));
    setInvertSelection(Utils.getFlag('V', options));
    setAttributeIndices(Utils.getOption('R', options));
  }

  /**
   * Calculates the distance between two instances.
   *
   * @param first
   *          the first instance
   * @param second
   *          the second instance
   * @return the distance between the two given instances
   */
  @Override
  public double distance(Instance first, Instance second) {

    if (m_Validated == false) {
      initializeRanges();
    }

    if (first.equalHeaders(second) == false) {
      System.err.println("Headers of the two instances doesn't match!!!");
      return Double.NaN;
    }

    int classidx = first.classIndex();
    double similarity = 0.0, product = 0.0, lengthA = 0.0, lengthB = 0.0, value = 0.0;
    int idx = 0;

    for (int v = 0; v < first.numValues(); v++) {
      idx = first.index(v);
      if (idx == classidx || !first.attributeSparse(v).isNumeric()) {
        continue;
      }

      if ((m_invertSelection && m_ActiveIndices[idx])
              || (m_invertSelection == false && m_ActiveIndices[idx] == false)) {
        continue;
      }

      if (m_binary) {
        ++lengthA;
        if (second.value(idx) != 0) {
          ++product;
        }
      }

      else {
        value = first.valueSparse(v);
        product += value * second.value(idx);
        lengthA += value * value;
      }
    }

    for (int v = 0; v < second.numValues(); v++) {
      idx = second.index(v);
      if (idx == classidx || !second.attributeSparse(v).isNumeric()) {
        continue;
      }

      if ((m_invertSelection && m_ActiveIndices[idx])
              || (m_invertSelection == false && m_ActiveIndices[idx] == false)) {
        continue;
      }

      if (m_binary) {
        ++lengthB;
      } else {
        value = second.valueSparse(v);
        lengthB += value * value;
      }
    }

    lengthA = Math.sqrt(lengthA);
    lengthB = Math.sqrt(lengthB);

    // empty instances
    if (lengthA == 0 || lengthB == 0) {
      return 1.0;
    }

    similarity = product / (lengthA * lengthB);

    if (Double.isNaN(similarity)) {
      // empty instances
      if (lengthA == 0 || lengthB == 0) {
        return 1.0;
      }

      System.out.println("similarity is NaN, product = " + product
              + ", lengthA = " + lengthA + ", lengthB = " + lengthB + "\nfirst: "
              + first + "\nsecond: " + second);
      System.err.println("similarity is NaN, product = " + product
              + ", lengthA = " + lengthA + ", lengthB = " + lengthB + "\nfirst: "
              + first + "\nsecond: " + second);

      try {
        throw new Exception();
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.exit(1);
      return 1;
    }
    if (Double.isInfinite(similarity)) {
      System.err.println("similarity is Infinite");
      return 0;
    }

    if (similarity < 0) {
      similarity = 0;
    }

    return 1 - similarity;
  }

  /**
   * Calculates the distance between two instances.
   *
   * @param first
   *          the first instance
   * @param second
   *          the second instance
   * @param stats
   *          the performance stats object
   * @return the distance between the two given instances
   */
  @Override
  public double distance(Instance first, Instance second, PerformanceStats stats)
          throws Exception {
    return distance(first, second, Double.POSITIVE_INFINITY, stats);
  }

  /**
   * Calculates the distance between two instances. Offers speed up (if the
   * distance function class in use supports it) in nearest neighbour search by
   * taking into account the cutOff or maximum distance. Depending on the
   * distance function class, post processing of the distances by
   * postProcessDistances(double []) may be required if this function is used.
   *
   * @param first
   *          the first instance
   * @param second
   *          the second instance
   * @param cutOffValue
   *          If the distance being calculated becomes larger than cutOffValue
   *          then the rest of the calculation is discarded.
   * @return the distance between the two given instances or
   *         Double.POSITIVE_INFINITY if the distance being calculated becomes
   *         larger than cutOffValue.
   */
  @Override
  public double distance(Instance first, Instance second, double cutOffValue) {
    return distance(first, second, cutOffValue, null);
  }

  /**
   * Calculates the distance between two instances. Offers speed up (if the
   * distance function class in use supports it) in nearest neighbour search by
   * taking into account the cutOff or maximum distance. Depending on the
   * distance function class, post processing of the distances by
   * postProcessDistances(double []) may be required if this function is used.
   *
   * @param first
   *          the first instance
   * @param second
   *          the second instance
   * @param cutOffValue
   *          If the distance being calculated becomes larger than cutOffValue
   *          then the rest of the calculation is discarded.
   * @param stats
   *          the performance stats object
   * @return the distance between the two given instances or
   *         Double.POSITIVE_INFINITY if the distance being calculated becomes
   *         larger than cutOffValue.
   */
  @Override
  public double distance(Instance first, Instance second, double cutOffValue,
                         PerformanceStats stats) {
    double distance = distance(first, second);
    if (distance > cutOffValue)
      return Double.POSITIVE_INFINITY;
    return distance;
  }

  /**
   * Initializes the ranges using all instances of the dataset. Sets m_Ranges.
   */
  protected void initializeRanges() {
    m_ActiveIndices = new boolean[m_Data.numAttributes()];
    if (m_Range == null) {
      for (int i = 0; i < m_ActiveIndices.length; i++) {
        m_ActiveIndices[i] = true;
      }
    } else {
      m_Range.setUpper(m_Data.numAttributes() - 1);
      m_Range.setInvert(m_invertSelection);
      for (int i = 0; i < m_ActiveIndices.length; i++) {
        m_ActiveIndices[i] = m_Range.isInRange(i);
      }
    }
    m_Validated = true;
  }

  @Override
  public void update(Instance ins) {

  }

  @Override
  public void clean() {

  }

  @Override
  public void postProcessDistances(double[] distances) {
  }

  /**
   * Sets the instances.
   *
   * @param data
   *          the instances to use
   */
  @Override
  public void setInstances(Instances data) {
    m_Data = data;
    m_Validated = false;
  }

  /**
   * returns the instances currently set.
   *
   * @return the current instances
   */
  @Override
  public Instances getInstances() {
    return m_Data;
  }

  /**
   * Gets the range of attributes used in the calculation of the distance.
   *
   * @return the attribute index range
   */
  public String getAttributeIndices() {
    if (m_Range == null)
      return "first-last";
    return m_Range.getRanges();
  }

  /**
   * Sets the range of attributes to use in the calculation of the distance. The
   * indices start from 1, 'first' and 'last' are valid as well. E.g.:
   * first-3,5,6-last
   *
   * @param value
   *          the new attribute index range
   */
  @Override
  public void setAttributeIndices(String value) {
    if (value.length() != 0) {
      if (m_Range.equals("first-last") == false)
        m_Range = new Range(value);
      m_Validated = false;
    }
  }

  /**
   * Returns the tip text for this property.
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String attributeIndicesTipText() {
    return "Specify range of attributes to act on. "
            + "This is a comma separated list of attribute indices, with "
            + "\"first\" and \"last\" valid values. Specify an inclusive "
            + "range with \"-\". E.g: \"first-3,5,6-10,last\".";
  }

  /**
   * Gets the range of attributes used in the calculation of the distance.
   *
   * @return the attribute index range
   */
  public boolean getInvertSelection() {
    return m_invertSelection;
  }

  /**
   * Sets whether the matching sense of attribute indices is inverted or not.
   *
   * @param value
   *          if true the matching sense is inverted
   */
  public void setInvertSelection(boolean value) {
    m_invertSelection = value;
    m_Validated = false;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String invertSelectionTipText() {
    return "Set attribute selection mode. If false, only selected "
            + "attributes in the range will be used in the distance calculation; if "
            + "true, only non-selected attributes will be used for the calculation.";
  }

  /**
   * Sets whether only counts an attribute's presence and absence, instead of
   * its frequency.
   *
   * @param binary
   *          if true the frequencies are not counted
   */
  public void setBinary(boolean binary) {
    m_binary = binary;
  }

  /**
   * Gets whether only attribute's presence and absence is counted. (default
   * false i.e. attribute frequencies are counted.)
   *
   * @return true if only binary values are used
   */
  public boolean getBinary() {
    return m_binary;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String binaryTipText() {
    return "Set to use binary presence and absence of attribute values instead of their "
            + "frequencies. If true, attribute weights are ignored.";
  }

}
