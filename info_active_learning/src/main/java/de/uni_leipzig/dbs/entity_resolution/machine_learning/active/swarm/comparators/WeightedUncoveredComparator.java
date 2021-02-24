package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.comparators;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Particle;

import java.util.Comparator;

/**
 * Created by christen on 27.06.2018.
 */
public class WeightedUncoveredComparator implements Comparator<Particle> {

  @Override
  public int compare(Particle o1, Particle o2) {
    double ambiguity1 = o1.getInfoMeasure();
    double ambiguity2 = o2.getInfoMeasure();
    if (ambiguity1 != ambiguity2) {
      return  -1 * Double.valueOf(ambiguity1).compareTo(Double.valueOf(ambiguity2));
    } else if (o1.getSameNeighbours() != o2.getSameNeighbours()) {
      return -1 * Double.valueOf(o1.getSameNeighbours()).compareTo(o2.getSameNeighbours());
    }
    else {
      return -1 * Double.valueOf(o1.getNotSameNeighbours()).compareTo(o2.getNotSameNeighbours());
    }
  }
}
