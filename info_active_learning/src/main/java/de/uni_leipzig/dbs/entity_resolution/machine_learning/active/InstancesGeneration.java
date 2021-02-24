package de.uni_leipzig.dbs.entity_resolution.machine_learning.active;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.Categories;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by christen on 13.02.2018.
 */
public class InstancesGeneration {


  public Instances generateDataset(Long2ObjectMap<Instance> instanceMap) {
    Instances dataset = getEmptyDataSet(instanceMap.values().iterator().next());
    for (Instance i : instanceMap.values()) {
      i.setDataset(dataset);
      dataset.add(i);
    }
    return dataset;
  }

  public Instances generateDatasetWithNominal(Long2ObjectMap<Instance> instanceMap) throws Exception {
    Instances dataset = getEmptyDataSetWithNominal(instanceMap.values().iterator().next());
    //r.setInputFormat(dataset);
    //r.setAttributeIndicesArray(new int[]{dataset.numAttributes()-1});
    //Map<Integer, String> classMap = new HashMap<>();
    //int index = 0;
    for (Instance i : instanceMap.values()) {
      Instance copy = new DenseInstance(i.numAttributes());
      copy.setDataset(dataset);
      for (int a = 0; a < i.numAttributes(); a++) {
        if (a != i.numAttributes()-1) {
          copy.setValue(a, i.value(a));
        }else {
          if (i.classValue() == 0)
            copy.setValue(a,"yes");
          else
            copy.setValue(a, "no");
        }
      }
      dataset.add(copy);
    }
    return dataset;
  }


  public Instances getEmptyDataSet(Instance inputFormat){
    ArrayList<Attribute> list = new ArrayList<>();
    int att = 0;
    for (int a = 0; a< inputFormat.numAttributes()-1;a++){
      list.add(new Attribute(inputFormat.attribute(a).name(), att++));
    }
    list.add(Categories.classAttribute);
    Instances dataset = new Instances("Dataset", list, 0);
    dataset.setClassIndex(list.size()-1);
    dataset.setClass(list.get(list.size()-1));
    return dataset;
  }

  public Instances getEmptyDataSetWithNominal(Instance inputFormat){
    ArrayList<Attribute> list = new ArrayList<>();
    int att = 0;
    for (int a = 0; a < inputFormat.numAttributes()-1;a++){
      list.add(new Attribute(inputFormat.attribute(a).name(), att++));
    }
    list.add(new Attribute("classNominal", Arrays.asList("yes", "no"),att));
    Instances dataset = new Instances("Dataset", list, 0);
    return dataset;
  }
}
