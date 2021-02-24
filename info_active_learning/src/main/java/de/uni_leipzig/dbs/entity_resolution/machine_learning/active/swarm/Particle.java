package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This represents a link that is specified by a similarity vector. A false positive or false negative particle moves
 * through the vector space according to centroid of the true positive particle. The goal is to get informative samples that
 * are close to the border of true positive and false positive
 */
public class Particle implements Comparable<Particle>{

  Logger log = Logger.getLogger(getClass());

  double[] vectorPosition;

  double[] moveVector;

  private long linkId;

  ParticleType type;

  private double speed;

  private double sim;

  private double drawProbability;

  private double draws;

  double sameNeighbours;

  double notSameNeighbors;

  double entropy;

  double ambiguityMeasure;

  double allDistance;

  public Particle(ParticleType pt, long linkId) {
    type = pt;
    this.linkId = linkId;
  }

  public double[] computeMoveVector(Swarm swarm, double speed) {
    double[] swarmCentroid = swarm.getCentroid();
    double[] moveVector;
    moveVector = difference(swarmCentroid, vectorPosition);
    for (int i = 0; i<moveVector.length; i++) {
      moveVector[i]*=speed;
    }
    return moveVector;
  }


  /**
   * utilizes the closest particle to compute the direction
   * @param swarm
   * @param speed
   * @return
   */
  public double[] computeMoveVectorToClosest(Swarm swarm, double speed) {
    double[] swarmCentroid = getClosest(vectorPosition, swarm);
    double[] moveVector;
    moveVector = difference(swarmCentroid, vectorPosition);
    for (int i = 0; i<moveVector.length; i++) {
      moveVector[i]*=speed;
    }
    return moveVector;
  }

  /**
   * utilizes the closest particle to compute the direction
   * @param swarm
   * @param speed
   * @return
   */
  public double[] computeMoveVectorToClosestK(Swarm swarm, double speed, int k) {
    double[] swarmCentroid = getClosestK(vectorPosition, swarm, k);
    double[] moveVector;
    moveVector = difference(swarmCentroid, vectorPosition);
    for (int i = 0; i<moveVector.length; i++) {
      moveVector[i] *= speed;
    }
    return moveVector;
  }




  public double[] difference(double[] vec1, double[] vec2) {
    double[] diffVec = new double[vec1.length];
    for (int i = 0; i <vec1.length; i++) {
      diffVec[i] = vec1[i] - vec2[i];
    }
    return diffVec;
  }

  public double[] add(double[] vec1, double[] vec2) {
    double[] diffVec = new double[vec1.length];
    for (int i = 0; i <vec1.length; i++) {
      diffVec[i] = vec1[i] + vec2[i];
    }
    return diffVec;
  }

  public double[] multiplyScalar(double[] vec1, double scalar) {
    double[] diffVec = new double[vec1.length];
    for (int i = 0; i <vec1.length; i++) {
      diffVec[i] = vec1[i] *scalar;
    }
    return diffVec;
  }

  public double euclideanDistance(double[] vec1) {
    double distance = 0;
    for (int i= 0; i<vec1.length; i++) {
      distance+=((this.vectorPosition[i] - vec1[i])*(this.vectorPosition[i] - vec1[i]));
    }
    distance = Math.sqrt(distance);
    return distance;
  }



  public double[] getVectorPosition() {
    return vectorPosition;
  }

  public double[] getClosest(double[] vec, Swarm swarm) {
    double closestSim = 0;
    double[] vectorSim = new double[vec.length];
    for (Particle p: swarm.getParticleList()) {
      double sim = computeSim(vectorPosition, p.getVectorPosition());
      if (sim > closestSim) {
        closestSim = sim;
        vectorSim = p.getVectorPosition();
      }
    }
    return vectorSim;
  }

  public double[] getClosestK(double[] vec, Swarm swarm, int k) {
    double closestSim = 0;
    double[] vectorSim = new double[vec.length];
    List<Pair> list = new ArrayList<>();
    for (Particle p: swarm.getParticleList()) {
      double sim = computeSim(p.getVectorPosition());
      if (sim != 1) {
        list.add(new Pair(p, sim));
      }
    }
    Collections.sort(list);
    double number = 0;
    for (int i = 0; i<k && i<list.size(); i++) {
      number++;
      vectorSim = add(vectorSim, list.get(i).p.getVectorPosition());
    }
    for (int i = 0; i < vectorSim.length; i++) {
      vectorSim[i] /= number;
    }
    return vectorSim;
  }

  public double computeSim(double[] vec2) {
    return computeSim(this.vectorPosition, vec2);
  }

  private double computeSim(double[] vec1, double[] vec2) {
    double vecProd = 0;
    double lengthFi = 0;
    double lengthCentroid = 0;
    for (int i = 0; i < vec1.length; i++) {
      vecProd += vec1[i]*vec2[i];
      lengthFi += vec1[i]*vec1[i];
      lengthCentroid += vec2[i] * vec2[i];
    }
    if (lengthFi == 0 || lengthCentroid == 0) {
      //log.info(Arrays.toString(centroid));
      return 0;
    }
    double cosineSim = vecProd/(Math.sqrt(lengthFi)*Math.sqrt(lengthCentroid));
    if (cosineSim < 0.00000001){
      //log.info("f:"+fi.toString());
      //log.info(Arrays.toString(centroid));
      return 0;
    }
    return cosineSim;
  }

  public void setVectorPosition(double[] vectorPosition) {
    this.vectorPosition = vectorPosition;
  }

  public long getLinkId() {
    return linkId;
  }

  public double[] getMoveVector() {
    return moveVector;
  }

  public void setMoveVector(double[] moveVector) {
    this.moveVector = moveVector;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }


  public double getSameNeighbours() {
    return sameNeighbours;
  }

  public void setSameNeighbours(double sameNeighbours) {
    this.sameNeighbours = sameNeighbours;
  }

  public double getNotSameNeighbours() {
    return notSameNeighbors;
  }

  public void setNotSameNeighbors(double notSameNeighbors) {
    this.notSameNeighbors = notSameNeighbors;
  }

  public double getAllDistance() {
    return allDistance;
  }

  public void setAllDistance(double allDistance) {
    this.allDistance = allDistance;
  }

  public double getEntropy() {
    return entropy;
  }

  public void setEntropy(double entropy) {
    this.entropy = entropy;
  }


  @Override
  public String toString() {
    return "Particle{" +
            "id=" + linkId +
            "type=" + type +
            ", sameNeighbours=" + sameNeighbours +
            ", notSameNeighbors=" + notSameNeighbors +
            ", entropy=" + entropy +
            ", ambiguityMeasure=" + ambiguityMeasure +
            ", allDistance=" + allDistance +
            '}';
  }

  public double getSim() {
    return sim;
  }

  public void setSim(double sim) {
    this.sim = sim;
  }

  public ParticleType getType() {
    return type;
  }

  public void setType(ParticleType type) {
    this.type = type;
  }

  public double getInfoMeasure() {
    return ambiguityMeasure;
  }

  public void setInfoMeasure(double ambiguityMeasure) {
    this.ambiguityMeasure = ambiguityMeasure;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Particle particle = (Particle) o;

    if (linkId != particle.linkId) return false;
    return Arrays.equals(vectorPosition, particle.vectorPosition);

  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(vectorPosition);
    result = 31 * result + (int) (linkId ^ (linkId >>> 32));
    return result;
  }




  class Pair implements Comparable<Pair>{
    Particle p;
    double sim;
    Pair(Particle p, double sim) {
      this.p = p;
      this.sim = sim;
    }

    @Override
    public int compareTo(Pair o) {
      return -1*Double.valueOf(this.sim).compareTo(Double.valueOf(o.sim));
    }
  }



  public double getDrawProbability() {
    return drawProbability;
  }

  public void setDrawProbability(double drawProbability) {
    this.drawProbability = drawProbability;
  }

  public double getDraws() {
    return draws;
  }

  public void setDraws(double draws) {
    this.draws = draws;
  }

  @Override
  public int compareTo(Particle o) {
    return -1*Double.valueOf(this.sim).compareTo(Double.valueOf(o.sim));
  }
}
