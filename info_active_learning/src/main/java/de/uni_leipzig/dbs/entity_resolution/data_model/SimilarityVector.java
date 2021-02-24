package de.uni_leipzig.dbs.entity_resolution.data_model;

import java.util.Arrays;

/**
 * Created by christen on 21.05.2017.
 */
public class SimilarityVector {


  /**
   * record id from first source
   */
  private int srcId;


  /**
   * record id from second source
   */
  private int targetId;


  /**
   * True if the two records are a match,<br>
   * False otherwise
   */
  private boolean isLink;

  /**
   * vector with similarity values between [0,1]
   */
  private double[] simVector;



  public SimilarityVector(int srcId, int targetId, boolean isLink, int vectorLength) {
    this.srcId = srcId;
    this.targetId = targetId;
    this.isLink = isLink;
    simVector = new double[vectorLength];
  }

  public boolean isLink() {
    return isLink;
  }

  public void setLink(boolean link) {
    isLink = link;
  }

  public double[] getSimVector() {
    return simVector;
  }

  public void setSimEntry(int index, double sim){
    simVector[index] = sim;
  }

  public void setSimVector(double[] simVector) {
    this.simVector = simVector;
  }

  public int getSrcId() {
    return srcId;
  }

  public int getTargetId() {
    return targetId;
  }

  @Override
  public String toString() {
    return "SimilarityVector{" +
            "srcId=" + srcId +
            ", targetId=" + targetId +
            ", isLink=" + isLink +
            ", simVector=" + Arrays.toString(simVector) +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SimilarityVector instance = (SimilarityVector) o;

    return Arrays.equals(simVector, instance.simVector);

  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(simVector);
  }
}
