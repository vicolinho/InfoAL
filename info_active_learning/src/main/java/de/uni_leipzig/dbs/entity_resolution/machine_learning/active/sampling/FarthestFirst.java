package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.sampling;

import de.uni_leipzig.dbs.entity_resolution.util.CosineSimComputation;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.apache.log4j.Logger;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Created by christen on 14.06.2018.
 */
public class FarthestFirst implements Sampling {

  Logger log = Logger.getLogger(getClass());

  @Override
  public LongSet getSample(Random r, Long2ObjectMap<Instance> vectors, Instances dataset, int sample) {
    return farthestFirstWekaImpl(r, vectors, dataset, sample);
  }

  private LongSet farthestFirstWekaImpl(Random r, Long2ObjectMap<Instance> vectors, Instances dataset, int sample) {
    List<Map.Entry<Long, Instance>> instanceList = new ArrayList<>(vectors.long2ObjectEntrySet());
    Collections.shuffle(instanceList, r);
    int startIndex = r.nextInt((instanceList.size())+1)+0;
    LongSet sampleSet = new LongOpenHashSet();
    boolean[] selected = new boolean[instanceList.size()];
    double[] minDistance = new double[instanceList.size()];
    for(int i = 0; i < minDistance.length; i++) {
      minDistance[i] = Double.MAX_VALUE;
    }

    Map.Entry<Long, Instance> startElement = instanceList.get(startIndex);
    selected[startIndex] = true;
    sampleSet.add(startElement.getKey().longValue());
    minDistance = updateMinDistance(minDistance, selected, instanceList, startElement.getValue());
    for(int i = 1; i < sample; i++) {
      log.info("selected:" + i);
      int nextI =  farthestAway(minDistance, selected);
      sampleSet.add(instanceList.get(nextI).getKey());
      selected[nextI] = true;
      updateMinDistance(minDistance, selected, instanceList, instanceList.get(nextI).getValue());
    }
    return sampleSet;
  }

  protected double[] updateMinDistance(double[] minDistance, boolean[] selected,
                                   List<Map.Entry<Long,Instance>> data, Instance center) {
    for(int i = 0; i<selected.length; i++) {
      if (!selected[i]) {
        double d = CosineSimComputation.euclideanDistance(center.toDoubleArray(),
                data.get(i).getValue().toDoubleArray());
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
