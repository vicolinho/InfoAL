package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christen on 18.02.2018.
 */
public class Swarm {

  public ParticleType type;

  private double[] centroid;

  private List<Particle> particleList;

  public Swarm() {
    particleList = new ArrayList<>();
  }

  public void computeCentroid() {
    double[] centroid = new double[particleList.get(0).getVectorPosition().length];
    for (Particle p: particleList) {
      centroid = p.add(centroid, p.getVectorPosition());
    }
    for (int i = 0; i < centroid.length; i++) {
      centroid[i]/=particleList.size();
    }
    this.centroid = centroid;
  }

  public double[] getCentroid() {
    return centroid;
  }

  public void addParticle(Particle p) {
    particleList.add(p);
  }


  public List<Particle> getParticleList() {
    return particleList;
  }

  @Override
  public String toString() {
    return "Swarm{" +
            "particleList=" + particleList +
            '}';
  }
}
