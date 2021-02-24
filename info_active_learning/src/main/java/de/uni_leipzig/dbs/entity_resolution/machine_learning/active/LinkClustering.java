package de.uni_leipzig.dbs.entity_resolution.machine_learning.active;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.feature.clustering.DistanceClustering;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.feature.clustering.distances.CosineDistance;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.*;
import weka.filters.unsupervised.attribute.Remove;

/**
 * Created by christen on 12.02.2018.
 */
public class LinkClustering {



  private DistanceFunction function;


  public LinkClustering(DistanceClustering distance){
    switch (distance) {
      case COSINE: function = new CosineDistance();
        break;
      case EUCLID: function = new EuclideanDistance();
        break;
      case MANHATTAN: function = new ManhattanDistance();
    }
  }

  public Int2ObjectMap<LongSet> computeLinkCluster(int k, Instances instances, Long2ObjectMap<Instance> long2InstanceMap)
          throws Exception {
    FilteredClusterer fc = new FilteredClusterer();
    Remove remove = new Remove();
    remove.setInputFormat(instances);
    remove.setAttributeIndicesArray(new int[]{instances.numAttributes()-1});
    fc.setFilter(remove);
    SimpleKMeans clusterer = new SimpleKMeans();
    clusterer.setMaxIterations(1000);
    clusterer.setNumClusters(k);
    clusterer.setNumExecutionSlots(16);
    clusterer.setDistanceFunction(function);
    clusterer.setSeed(503);
    fc.setClusterer(clusterer);
    fc.buildClusterer(instances);
    Int2ObjectMap<LongSet> clusterMap = new Int2ObjectOpenHashMap<>();
    for (Long2ObjectMap.Entry<Instance> e: long2InstanceMap.long2ObjectEntrySet()) {
      int cid = fc.clusterInstance(e.getValue());
      LongSet cluster = clusterMap.get(cid);
      if (cluster == null) {
        cluster = new LongOpenHashSet();
        clusterMap.put(cid, cluster);
      }
      cluster.add(e.getLongKey());
    }
    return clusterMap;
  }
}
