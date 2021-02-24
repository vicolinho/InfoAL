package de.uni_leipzig.dbs.entity_resolution.machine_learning.active;

import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;

/**
 * Created by christen on 12.02.2018.
 */
public class Oracle {


   LongSet truePositives;

  public static Oracle instance;

  public Oracle(){

  }

  public static Oracle getInstance(){
    if (instance == null) {
      instance = new Oracle();
    }
    return instance;
  }



  public void initialize(LongSet tps) {
    this.truePositives = tps;
  }

  public boolean isMatch(long id) {
    return truePositives.contains(id);
  }

  public Long2IntMap classifySimVectorPairs(LongSet unlabeledVectors) {
    Long2IntMap classifiedVectors = new Long2IntOpenHashMap();
    for (long pairId : unlabeledVectors) {
      int isLink = (truePositives.contains(pairId))? 1 : 0;
      classifiedVectors.put(pairId, isLink);
    }
    return classifiedVectors;
  }
}
