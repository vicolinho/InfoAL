package de.uni_leipzig.dbs.entity_resolution.examples.datasets;

import de.uni_leipzig.dbs.entity_resolution.data_model.SimilarityVector;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.Categories;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


import java.util.ArrayList;
import java.util.Map;

/**
 * Transformation to weka instances and store index of pair
 */
public class TrainingInstance2Instances {

  private Long2ObjectMap<Instance> link2InstanceMap;

  public Instances createInstances(Map<Long, SimilarityVector> trainingInstanceMap) {
    link2InstanceMap = new Long2ObjectOpenHashMap<>();
    ArrayList<Attribute> list = new ArrayList<>();
    int att = 0;
    int attributeCount = trainingInstanceMap.values().iterator().next().getSimVector().length;
    for (int i=0; i<attributeCount; i++) {
      list.add(new Attribute("att"+i, att++));
    }
    list.add(Categories.classAttribute);
    Instances dataset = new Instances("Dataset", list, 0);
    dataset.setClassIndex(attributeCount);
    dataset.setClass(list.get(list.size()-1));
    for (long pair : trainingInstanceMap.keySet()) {

      SimilarityVector ti = trainingInstanceMap.get(pair);
      Instance i = createInstance(ti, dataset);
      link2InstanceMap.put(pair, i);
      dataset.add(i);
    }
    return dataset;
  }

  private Instance createInstance(SimilarityVector ti, Instances ds) {
    Instance inst = new DenseInstance(ti.getSimVector().length + 1);
    for (int i = 0; i < ti.getSimVector().length; i++) {
      inst.setValue(i, ti.getSimVector()[i]);
    }
    inst.setDataset(ds);
    if (ti.isLink()) {
      inst.setValue(ti.getSimVector().length, "yes");
    } else {
      inst.setValue(ti.getSimVector().length, "no");
    }
    return inst;
  }

  public Long2ObjectMap<Instance> getLink2InstanceMap() {
    return link2InstanceMap;
  }
}
