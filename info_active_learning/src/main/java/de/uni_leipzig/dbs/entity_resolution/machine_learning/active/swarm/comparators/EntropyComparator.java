package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.comparators;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Pair;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Particle;

import java.util.Comparator;

/**
 * Created by christen on 29.09.2018.
 */
public class EntropyComparator implements Comparator<Particle> {

@Override
public int compare(Particle o1, Particle o2) {

        double ambiguity1 = o1.getEntropy();
        double ambiguity2 = o2.getEntropy();
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
