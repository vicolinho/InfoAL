package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm;

/**
 * Created by christen on 19.06.2018.
 */
public class Pair implements Comparable<Pair> {

  private double sim;

  private Particle p;

  private Particle originParticle;


  public Pair(Particle p, double sim) {
    this.p = p;
    this.sim = sim;
  }

  public double getSim() {
    return sim;
  }

  public void setSim(double sim) {
    this.sim = sim;
  }

  public Particle getP() {
    return p;
  }

  public void setP(Particle p) {
    this.p = p;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Pair pair = (Pair) o;

    return p.equals(pair.p);

  }

  public Particle getOriginParticle() {
    return originParticle;
  }

  public void setOriginParticle(Particle originParticle) {
    this.originParticle = originParticle;
  }

  @Override
  public int hashCode() {
    return p.hashCode();
  }

  @Override
  public int compareTo(Pair o) {
    return -1*Double.valueOf(this.sim).compareTo(Double.valueOf(o.sim));
  }

  @Override
  public String toString() {
    return "Pair{" +
            "sim=" + sim +
            ", p=" + p +
            '}';
  }
}
