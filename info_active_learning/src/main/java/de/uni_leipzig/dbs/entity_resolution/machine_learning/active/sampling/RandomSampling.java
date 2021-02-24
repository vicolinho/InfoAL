package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.sampling;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Created by christen on 28.09.2018.
 */
public class RandomSampling implements Sampling {
  @Override
  public LongSet getSample(Random r, Long2ObjectMap<Instance> vectors, Instances instances, int sample) {
    List<Map.Entry<Long, Instance>> instanceList = new ArrayList<>(vectors.long2ObjectEntrySet());
    Collections.shuffle(instanceList, r);
    LongSet set = new LongOpenHashSet();
    for (Map.Entry<Long, Instance> e : instanceList.subList(0, sample)) {
      set.add(e.getKey());
    }
    return set;
  }
}
