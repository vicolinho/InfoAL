package de.uni_leipzig.dbs.entity_resolution.machine_learning;

import de.uni_leipzig.dbs.formRepository.dataModel.export.ToolProperties;
import weka.classifiers.Classifier;

import java.io.*;
import java.util.List;

/**
 * Created by christen on 07.06.2017.
 */
public class MLModel implements Comparable<MLModel>, Serializable{

  private double correctClasses = 0;

  private double fmeasure = 0;

  private Classifier classifier;


  private double[] centroidValues;

  private List<ToolProperties> combination;


  public void writeClassifier(String file) throws IOException {
    ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(file));
    oos.writeObject(classifier);
    oos.flush();
    oos.close();
  }

  public List<ToolProperties> getCombination() {
    return combination;
  }

  public void setCombination(List<ToolProperties> combination) {
    this.combination = combination;
  }


  public void readClassifier (String file) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream(file));
    classifier = (Classifier) ois.readObject();
    ois.close();
  }

  public Classifier getClassifier() {
    return classifier;
  }

  public void setClassifier(Classifier classifier) {
    this.classifier = classifier;
  }

  public double getCorrectClasses() {
    return correctClasses;
  }

  public void setCorrectClasses(double correctClasses) {
    this.correctClasses = correctClasses;
  }

  public double getFmeasure() {
    return fmeasure;
  }

  public void setFmeasure(double fmeasure) {
    this.fmeasure = fmeasure;
  }

  @Override
  public int compareTo(MLModel o) {
    if (correctClasses<o.correctClasses){
      return 1;
    }else if (correctClasses>o.correctClasses){
      return -1;
    }else
      return 0;
  }

  @Override
  public String toString() {
    return "MLModel{" +
            "fmeasure=" + fmeasure +
            ", combination=" + combination +
            '}';
  }


  public double[] getCentroidValues() {
    return centroidValues;
  }

  public void setCentroidValues(double[] centroidValues) {
    this.centroidValues = centroidValues;
  }
}
