package de.uni_leipzig.dbs.entity_resolution.machine_learning.classification;

import de.uni_leipzig.dbs.entity_resolution.data_model.SimilarityVector;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.Categories;
import de.uni_leipzig.dbs.formRepository.dataModel.AnnotationMapping;
import de.uni_leipzig.dbs.formRepository.dataModel.EntityAnnotation;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by christen on 05.10.2017.
 */
public class BaselineClassification {

  public AnnotationMapping predictLinks(Long2ObjectMap<SimilarityVector> simVectors,
                                        Classifier classifier)
          throws Exception {
    AnnotationMapping annotationMapping = new AnnotationMapping();
    Long2ObjectMap<Instance> simVectorsInstances = createInstances(simVectors);
    for (Long2ObjectMap.Entry<SimilarityVector> e : simVectors.long2ObjectEntrySet()){
      Instance i = simVectorsInstances.get(e.getLongKey());
      if (i != null) {
        double c = classifier.classifyInstance(i);
        String category = i.classAttribute().value((int) c);
        if (category.equals("yes")) {
        annotationMapping.addAnnotation(new EntityAnnotation(e.getValue().getSrcId(), e.getValue().getTargetId(),
                null, null, 1, false));
        }
      }
    }
    return annotationMapping;
  }

  private Long2ObjectMap<Instance> createInstances(Map<Long, SimilarityVector> trainingInstanceMap) {
    ArrayList<Attribute> list = new ArrayList<>();
    int att = 0;
    Long2ObjectMap<Instance> instanceLong2ObjectMap = new Long2ObjectOpenHashMap<>();
    int attributeCount = trainingInstanceMap.values().iterator().next().getSimVector().length;
    for (int i=0; i<attributeCount; i++) {
      list.add(new Attribute("att"+i, att++));
    }
    list.add(Categories.classAttribute);
    Instances dataset = new Instances("Dataset", list, 0);
    dataset.setClassIndex(attributeCount);
    dataset.setClass(list.get(list.size()-1));
    for (Map.Entry<Long, SimilarityVector> e: trainingInstanceMap.entrySet()) {
      Instance i = createInstance(e.getValue(), dataset);
      instanceLong2ObjectMap.put(e.getKey().longValue(), i);

    }
    return instanceLong2ObjectMap;
  }

  private Instance createInstance(SimilarityVector ti, Instances ds) {
    Instance inst = new DenseInstance(ti.getSimVector().length + 1);
    for (int i = 0; i < ti.getSimVector().length; i++) {
      inst.setValue(i, ti.getSimVector()[i]);
    }
    inst.setDataset(ds);
    inst.setValue(ti.getSimVector().length, "no");
    return inst;
  }
}
