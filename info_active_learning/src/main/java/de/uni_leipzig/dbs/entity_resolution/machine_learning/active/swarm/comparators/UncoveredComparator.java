package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.comparators;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Particle;

import java.util.Comparator;

/**
 * Created by christen on 21.06.2018.
 */
public class UncoveredComparator implements Comparator<Particle> {

  @Override
  public int compare(Particle o1, Particle o2) {


    double ambiguity1 = o1.getNotSameNeighbours();
    double ambiguity2 = o2.getNotSameNeighbours();
    if (ambiguity1 != ambiguity2) {
      return -1 * Double.valueOf(ambiguity1).compareTo(Double.valueOf(ambiguity2));
    } else
      return Double.valueOf(o1.getSameNeighbours()).compareTo(o2.getSameNeighbours());
  }
}
