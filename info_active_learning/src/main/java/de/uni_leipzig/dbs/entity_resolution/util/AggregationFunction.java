package de.uni_leipzig.dbs.entity_resolution.util;

import java.util.List;

public enum AggregationFunction {

  
  MAX
 
 {
    @Override
    public float aggregateFloatList(List<Float> list) {
      float currentMax = Float.MIN_VALUE;
      for (Float f: list){
        currentMax = (f>currentMax)?f:currentMax;
      }
      return currentMax;
    }
  },  
  AVG
  {
    @Override
    public float aggregateFloatList(List<Float> list) {
      float avg =0;
      for (Float f: list){
        avg+=f;
      }
      avg/=list.size();
      return avg;
    }
  },  
  MIN
  {
    @Override
    public float aggregateFloatList(List<Float> list) {
      float currentMin = Float.MAX_VALUE;
      for (Float f: list){
        currentMin = (f<currentMin)?f:currentMin;
      }
      return currentMin;
    }
  };  
  
  public abstract float aggregateFloatList (List<Float> list);
}
