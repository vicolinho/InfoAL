package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.sampling;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.LinkClustering;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.feature.clustering.DistanceClustering;
import de.uni_leipzig.dbs.entity_resolution.util.CosineSimComputation;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.apache.log4j.Logger;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Created by christen on 19.06.2018.
 */
public class StratifiedSampling implements Sampling {

  private final LinkClustering clustering;

  int k;

  private StratifiedSampling(int k, DistanceClustering distanceClustering) {
    this.k = k;
    clustering = new LinkClustering(distanceClustering);
  }

  @Override
  public LongSet getSample(Random r, Long2ObjectMap<Instance> vectors, Instances instances, int sample) {
    Int2ObjectMap<LongSet> clusters = new Int2ObjectOpenHashMap<>();
    try {
      clusters = clustering.computeLinkCluster(k,  instances, vectors);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Int2ObjectMap<double[]> centroids = computeCentroids(clusters, vectors);
    Int2ObjectMap<List<Pair>> sortedInstancesPerCluster = computeMostVariantInstances(centroids, clusters,
            vectors);
    LongSet selected = selectInstances(sortedInstancesPerCluster, sample);
    return selected;
  }

  private LongSet selectInstances(Int2ObjectMap<List<Pair>> sortedInstancesPerCluster,
                                  int sample) {
    List<Map.Entry<Integer, List<Pair>>> list = new ArrayList<>(sortedInstancesPerCluster.int2ObjectEntrySet());
    int index = 0;
    LongSet selected = new LongOpenHashSet();
    do {
      Map.Entry<Integer, List<Pair>> e = list.get(index);
      if (e.getValue().size() > 0) {
        Pair p = e.getValue().remove(0);
        selected.add(p.link);
      }
      index = (index+1)%list.size();
    } while(selected.size() < sample);
    return selected;
  }

  private Int2ObjectMap<double[]> computeCentroids(Int2ObjectMap<LongSet> clusters, Long2ObjectMap<Instance> vectors) {
    Int2ObjectMap<double[]> clusterCentroids = new Int2ObjectOpenHashMap<>();
    for(Map.Entry<Integer, LongSet> e: clusters.int2ObjectEntrySet()) {
      double[] centroid = null;
      for (long linkId : e.getValue()) {
        Instance i = vectors.get(linkId);
        if (centroid == null) {
          centroid = new double[i.numAttributes()-1];
          clusterCentroids.put(e.getKey().intValue(), centroid);
        }
        for (int a = 0; a < i.numAttributes()-1; a++){
          centroid[a] +=i.value(a);
        }
      }// all links
      for (int a = 0; a < centroid.length; a++){
        centroid[a] /= (double) e.getValue().size();
      }
    }
    return clusterCentroids;
  }

  public Int2ObjectMap<List<Pair>> computeMostVariantInstances(Int2ObjectMap<double[]> centroids,
      Int2ObjectMap<LongSet> clusters, Long2ObjectMap<Instance> vectors) {
    Int2ObjectMap<List<Pair>> clusterListSorted = new Int2ObjectOpenHashMap<>();
    for (Map.Entry<Integer, double[]> e: centroids.int2ObjectEntrySet()) {
      double[] centroid = e.getValue();
      LongSet ids = clusters.get(e.getKey().intValue());
      List<Pair> list = new ArrayList<>();
      clusterListSorted.put(e.getKey().intValue(), list);
      for (long l : ids) {
        Instance i = vectors.get(l);
        double[] vec =  new double[i.toDoubleArray().length-1];
        for (int vecIndex = 0; vecIndex  < vec.length; vecIndex++) {
          vec[vecIndex] = i.value(vecIndex);
        }
        double sim = CosineSimComputation.sim(centroid, vec);
        list.add(new Pair(l, sim));
      }
      Collections.sort(list);
    }
    return clusterListSorted;
  }

  private class Pair implements Comparable<Pair> {

    long link;

    double sim;

    public Pair (long linkId, double sim) {
      this.link = linkId;
      this.sim = sim;
    }

    @Override
    public int compareTo(Pair o) {
      return -1* Double.valueOf(sim).compareTo(o.sim);
    }
  }

  public static class Builder {
    int k = 5;

    DistanceClustering distance = DistanceClustering.COSINE;
    public Builder(){

    }

    public Builder k(int k) {
      this.k = k;
      return this;
    }

    public Builder distance(DistanceClustering distanceClustering) {
      this.distance = distanceClustering;
      return this;
    }

    public StratifiedSampling build(){
      return new StratifiedSampling(k, distance);
    }
  }
}
