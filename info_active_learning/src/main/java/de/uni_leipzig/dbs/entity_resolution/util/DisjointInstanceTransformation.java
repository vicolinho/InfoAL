package de.uni_leipzig.dbs.entity_resolution.util;

import de.uni_leipzig.dbs.entity_resolution.data_model.SimilarityVector;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by christen on 11.07.2018.
 */
public class DisjointInstanceTransformation {


  public static Long2ObjectMap<SimilarityVector> disjoint(Long2ObjectMap<SimilarityVector> instances) {
    Long2ObjectMap<SimilarityVector> disjointTrainingInstances = new Long2ObjectOpenHashMap<>();
    Set<SimilarityVector> set = new HashSet<>();
    for (Map.Entry<Long, SimilarityVector> e: instances.long2ObjectEntrySet()) {
      if (!set.contains(e.getValue())) {
        set.add(e.getValue());
        disjointTrainingInstances.put(e.getKey().longValue(), e.getValue());
      }
    }
    return disjointTrainingInstances;
  }
}
