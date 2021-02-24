package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.selection;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Pair;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Particle;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by christen on 16.08.2018.
 */
public class FarthestFirstSelection {

  Logger log = Logger.getLogger(getClass());

  public List<Pair> farthestFirstWekaImpl(List<Pair> orderedList, int upperBound) {

    int startIndex = 0;
    List<Pair> sampleSet = new ArrayList<>();

    if (orderedList.size() != 0) {
      boolean[] selected = new boolean[orderedList.size()];
      double[] minDistance = new double[orderedList.size()];
      for(int i = 0; i < minDistance.length; i++) {
        minDistance[i] = Double.MAX_VALUE;
      }
      Pair startElement = orderedList.get(startIndex);
      selected[startIndex] = true;
      sampleSet.add(startElement);
      minDistance = updateMinDistance(minDistance, selected, orderedList, startElement);
      for (int i = 1; i < upperBound && i < orderedList.size(); i++) {
        int nextI = farthestAway(minDistance, selected);
        sampleSet.add(orderedList.get(nextI));
        selected[nextI] = true;
        updateMinDistance(minDistance, selected, orderedList, orderedList.get(nextI));
      }
    }
    return sampleSet;
  }

  public List<Pair> farthestFirstWekaImpl(List<Pair> orderedList, int upperBound, int index) {

    int startIndex = index;
    List<Pair> sampleSet = new ArrayList<>();

    if (orderedList.size() != 0) {
      boolean[] selected = new boolean[orderedList.size()];
      double[] minDistance = new double[orderedList.size()];
      for(int i = 0; i < minDistance.length; i++) {
        minDistance[i] = Double.MAX_VALUE;
      }
      Pair startElement = orderedList.get(startIndex);
      selected[startIndex] = true;
      sampleSet.add(startElement);
      minDistance = updateMinDistance(minDistance, selected, orderedList, startElement);
      for (int i = 1; i < upperBound && i < orderedList.size(); i++) {
        int nextI = farthestAway(minDistance, selected);
        sampleSet.add(orderedList.get(nextI));
        selected[nextI] = true;
        updateMinDistance(minDistance, selected, orderedList, orderedList.get(nextI));
      }
    }
    return sampleSet;
  }

  public List<Particle> farthestFirstParticleWekaImpl(List<Particle> orderedList, int upperBound) {

    int startIndex = 0;
    List<Particle> sampleSet = new ArrayList<>();

    if (orderedList.size() != 0) {
      boolean[] selected = new boolean[orderedList.size()];
      double[] minDistance = new double[orderedList.size()];
      for(int i = 0; i < minDistance.length; i++) {
        minDistance[i] = Double.MAX_VALUE;
      }
      Particle startElement = orderedList.get(startIndex);
      selected[startIndex] = true;
      sampleSet.add(startElement);
      minDistance = updateMinDistance(minDistance, selected, orderedList, startElement);
      for (int i = 1; i < upperBound && i < orderedList.size(); i++) {
        //log.info("selected:" + i);
        int nextI = farthestAway(minDistance, selected);
        sampleSet.add(orderedList.get(nextI));
        selected[nextI] = true;
        updateMinDistance(minDistance, selected, orderedList, orderedList.get(nextI));
      }
    }
    return sampleSet;
  }

  protected double[] updateMinDistance(double[] minDistance, boolean[] selected,
                                       List<Particle> orderedList, Particle center) {
    for(int i = 0; i<selected.length; i++) {
      if (!selected[i]) {
        double d = center.euclideanDistance(orderedList.get(i).getVectorPosition());
        if (d < minDistance[i]) {
          minDistance[i] = d;
        }
      }
    }
    return minDistance;
  }

  protected double[] updateMinDistance(double[] minDistance, boolean[] selected,
                                       List<Pair> orderedList, Pair center) {
    for(int i = 0; i<selected.length; i++) {
      if (!selected[i]) {
        double d = center.getP().euclideanDistance(orderedList.get(i).getP().getVectorPosition());
        if (d < minDistance[i])
          minDistance[i] = d;
      }
    }
    return minDistance;
  }

  protected int farthestAway(double[] minDistance, boolean[] selected) {
    double maxDistance = -1.0;
    int maxI = -1;
    for(int i = 0; i < selected.length; i++)
      if (!selected[i])
        if (maxDistance < minDistance[i]) {
          maxDistance = minDistance[i];
          maxI = i;
        }
    return maxI;
  }
}
