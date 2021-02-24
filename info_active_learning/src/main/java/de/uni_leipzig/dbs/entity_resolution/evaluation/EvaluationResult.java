package de.uni_leipzig.dbs.entity_resolution.evaluation;

import de.uni_leipzig.dbs.formRepository.dataModel.AnnotationMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EvaluationResult {

  private Map<String, Float> measures;

  public Map<String, Float> getMeasures() {
    return measures;
  }

  public void setMeasures(Map<String, Float> measures) {
    this.measures = measures;
  }

  private AnnotationMapping falsePositives;

  private AnnotationMapping falseNegative;

  private AnnotationMapping truePositive;

  private String srcName;

  private String targetName;

  private long[] annotationTimes;

  private long[] selectionTimes;

  private long[] preprocessingTimes;


  public EvaluationResult(String srcName, String targetName, long[] umlsTimes,
      long[] preprocessingTimes, long[] annotationTimes, long[] selectionTimes) {
    this.setSrcName(srcName);
    this.setTargetName(targetName);
    this.setPreprocessingTimes(preprocessingTimes);
    this.setAnnotationTimes(annotationTimes);
    this.setSelectionTimes(selectionTimes);
    this.measures = new HashMap<>();
    measures.put("precision", -1f);
    measures.put("recall", -1f);
    measures.put("fmeasure", -1f);
  }

  public EvaluationResult(String srcName, String targetName, long[] annotationTimes, long[] selectionTimes) {
    this.setSrcName(srcName);
    this.setTargetName(targetName);
    this.setAnnotationTimes(annotationTimes);
    this.setSelectionTimes(selectionTimes);
    this.measures = new HashMap<>();
    measures.put("precision", -1f);
    measures.put("recall", -1f);
    measures.put("fmeasure", -1f);
  }

  public EvaluationResult(String srcName, String targetName) {
    this.setSrcName(srcName);
    this.setTargetName(targetName);
    this.measures = new HashMap<String, Float>();
    measures.put("precision", -1f);
    measures.put("recall", -1f);
    measures.put("fmeasure", -1f);
  }

  public AnnotationMapping getFalsePositives() {
    return falsePositives;
  }


  public void setFalsePositives(AnnotationMapping falsePositives, int correctAnnos) {
    this.falsePositives = falsePositives;
    float precision = (correctAnnos == 0) ? 0 : (correctAnnos /
            ((float) correctAnnos + falsePositives.getNumberOfAnnotations()));
    this.measures.put("precision", precision);
  }


  public AnnotationMapping getFalseNegative() {
    return falseNegative;
  }


  public void setFalseNegative(AnnotationMapping falseNegative, int correctAnnos) {
    this.falseNegative = falseNegative;
    float recall = (correctAnnos == 0) ? 0 : (correctAnnos /
            ((float) correctAnnos + falseNegative.getNumberOfAnnotations()));
    this.measures.put("recall", recall);
  }

  public void setMinimalCoveragePerQuestion(AnnotationMapping truePositive, Set<Integer> questionIds) {
    float oneCorrectAnno = 0;
    for (Integer qid : questionIds) {
      if (truePositive.containsCorrespondingTargetIds(qid)) {
        oneCorrectAnno++;
      }
    }
    float coverage = 0;
    coverage = oneCorrectAnno / questionIds.size();
    this.measures.put("minimalCoverage", coverage);
  }


  public void calculateFmeasure() {
    float fmeasure = 2 * this.measures.get("precision") * this.measures.get("recall") /
            (this.measures.get("precision") + this.measures.get("recall"));
    this.measures.put("fmeasure", fmeasure);
  }

  @Override
  public String toString() {
    return truePositive.getNumberOfAnnotations() + System.getProperty("line.separator") +
            falsePositives.getNumberOfAnnotations() + System.getProperty("line.separator") +
            falseNegative.getNumberOfAnnotations() + System.getProperty("line.separator") +
            this.measures.get("precision") + System.getProperty("line.separator") +
            this.measures.get("recall") + System.getProperty("line.separator") +
            this.measures.get("fmeasure") + System.getProperty("line.separator");
  }

  public String[] toStringArray() {
    String[] resVec = new String[]{truePositive.getNumberOfAnnotations() + "",
            falsePositives.getNumberOfAnnotations() + "",
            falseNegative.getNumberOfAnnotations() + "",
            this.measures.get("precision") + "",
            this.measures.get("recall") + "",
            this.measures.get("fmeasure") + ""};
    return resVec;
  }

  public String getSrcName() {
    return srcName;
  }


  public void setSrcName(String srcName) {
    this.srcName = srcName;
  }


  public String getTargetName() {
    return targetName;
  }


  public void setTargetName(String targetName) {
    this.targetName = targetName;
  }


  public AnnotationMapping getTruePositive() {
    return truePositive;
  }


  public void setTruePositive(AnnotationMapping truePositive) {
    this.truePositive = truePositive;
  }


  public void setPreprocessingTimes(long[] preprocessingTimes) {
    this.preprocessingTimes = preprocessingTimes;
  }

  public void setAnnotationTimes(long[] annotationTimes) {
    this.annotationTimes = annotationTimes;
  }

  public void setSelectionTimes(long[] selectionTimes) {
    this.selectionTimes = selectionTimes;
  }




}
