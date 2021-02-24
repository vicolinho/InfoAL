package de.uni_leipzig.dbs.entity_resolution.util;

import de.uni_leipzig.dbs.formRepository.dataModel.AnnotationMapping;
import de.uni_leipzig.dbs.formRepository.dataModel.EntityAnnotation;
import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the set operations (union, intersection, difference) for two annotation mappings
 *
 * @author christen
 */
public class SetAnnotationOperator {

  static Logger log = Logger.getLogger(SetAnnotationOperator.class);

  public static AnnotationMapping union(AggregationFunction func,
                                        AnnotationMapping mapping1, AnnotationMapping mapping2) {

    AnnotationMapping unionMapping = new AnnotationMapping(mapping1.getSrcVersion(), mapping1.getTargetVersion());
    for (EntityAnnotation ea : mapping1.getAnnotations()) {
      if (!mapping2.contains(ea)) {
        EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                ea.getSrcAccession(), ea.getTargetAccession(), ea.getSim(), ea.isVerfified());
        copy.setSimVector(ea.getSimVector());
        unionMapping.addAnnotation(copy);
      } else {
        List<Float> sims = new ArrayList<Float>();
        sims.add(ea.getSim());
        sims.add(mapping2.getAnnotation(ea.getId()).getSim());
        float aggSim = func.aggregateFloatList(sims);
        EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                ea.getSrcAccession(), ea.getTargetAccession(), aggSim, ea.isVerfified());
        copy.setSimVector(ea.getSimVector());
        unionMapping.addAnnotation(copy);
      }
    }
    for (EntityAnnotation ea : mapping2.getAnnotations()) {
      if (!unionMapping.contains(ea))
        if (!mapping1.contains(ea)) {
          EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                  ea.getSrcAccession(), ea.getTargetAccession(), ea.getSim(), ea.isVerfified());
          copy.setSimVector(ea.getSimVector());
          unionMapping.addAnnotation(copy);
        }
    }
    return unionMapping;
  }


  public static AnnotationMapping intersect(AggregationFunction func,
                                            AnnotationMapping mapping1, AnnotationMapping mapping2) {
    AnnotationMapping unionMapping = new AnnotationMapping(mapping1.getSrcVersion(), mapping1.getTargetVersion());
    for (EntityAnnotation ea : mapping1.getAnnotations()) {
      if (mapping2.contains(ea)) {
        List<Float> sims = new ArrayList<Float>();
        sims.add(ea.getSim());
        sims.add(mapping2.getAnnotation(ea.getId()).getSim());
        float aggSim = func.aggregateFloatList(sims);
        EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                ea.getSrcAccession(), ea.getTargetAccession(), aggSim, ea.isVerfified());
        copy.setSimVector(ea.getSimVector());
        unionMapping.addAnnotation(copy);
      }
    }
    return unionMapping;
  }


  public static AnnotationMapping diff(
          AnnotationMapping mapping1, AnnotationMapping mapping2) {
    AnnotationMapping unionMapping = new AnnotationMapping(mapping1.getSrcVersion(), mapping1.getTargetVersion());
    for (EntityAnnotation ea : mapping1.getAnnotations()) {
      if (!mapping2.contains(ea)) {
        EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                ea.getSrcAccession(), ea.getTargetAccession(), ea.getSim(), ea.isVerfified());
        unionMapping.addAnnotation(copy);
      }
    }
    return unionMapping;
  }

  public static AnnotationMapping unionWithVector(AggregationFunction func,
                                        AnnotationMapping mapping1, AnnotationMapping mapping2) {

    AnnotationMapping unionMapping = new AnnotationMapping(mapping1.getSrcVersion(), mapping1.getTargetVersion());

    int max1 = 0;
    int max2 = 0;
    for (EntityAnnotation ea: mapping1.getAnnotations()) {
      if (ea.getSimVector() != null) {
        max1 = ea.getSimVector().length;
      }
    }
    if (max1 == 0) {
      max1 = 1;
    }

    for (EntityAnnotation ea: mapping2.getAnnotations()) {
      if (ea.getSimVector() != null) {
        max2 = ea.getSimVector().length;
      }
    }
    if (max2 == 0) {
      max2 = 1;
    }

    for (EntityAnnotation ea : mapping1.getAnnotations()) {
      float[] combinedVec = new float[max1+max2];
      float[] vec1 = ea.getSimVector();
      if (vec1 == null) {
        combinedVec[0] = ea.getSim();
      }else {
        for (int i = 0; i < max1; i++) {
          combinedVec[i] = ea.getSimVector()[i];
        }
      }
      if (!mapping2.contains(ea)) {
        EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                ea.getSrcAccession(), ea.getTargetAccession(), ea.getSim(), ea.isVerfified());
        unionMapping.addAnnotation(copy);
        copy.setSimVector(combinedVec);
      } else {
        EntityAnnotation ea2 = mapping2.getAnnotation(ea.getId());
        float[] vec2 = ea2.getSimVector();
        if (vec2 == null) {
          combinedVec[max1] = ea2.getSim();
        }else {
          for (int i = 0; i < max2; i++) {
            combinedVec[i+max1] = ea.getSimVector()[i];
          }
        }
        List<Float> sims = new ArrayList<Float>();
        sims.add(ea.getSim());
        sims.add(mapping2.getAnnotation(ea.getId()).getSim());
        float aggSim = func.aggregateFloatList(sims);
        EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                ea.getSrcAccession(), ea.getTargetAccession(), aggSim, ea.isVerfified());
        copy.setSimVector(combinedVec);
        unionMapping.addAnnotation(copy);
      }
    }
    for (EntityAnnotation ea : mapping2.getAnnotations()) {
      float[] combinedVec = new float[max1+max2];
      float[] vec1 = ea.getSimVector();

      if (vec1 == null) {
        combinedVec[max1] = ea.getSim();
      }else {
        for (int i = 0; i < max2; i++) {
          combinedVec[i+max1] = ea.getSimVector()[i];
        }
      }
      if (!unionMapping.contains(ea))
        if (!mapping1.contains(ea)) {
          EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                  ea.getSrcAccession(), ea.getTargetAccession(), ea.getSim(), ea.isVerfified());
          unionMapping.addAnnotation(copy);
          copy.setSimVector(combinedVec);
        }
    }
    return unionMapping;
  }


  public static AnnotationMapping intersectWithVector(AggregationFunction func,
                                            AnnotationMapping mapping1, AnnotationMapping mapping2) {
    AnnotationMapping unionMapping = new AnnotationMapping(mapping1.getSrcVersion(), mapping1.getTargetVersion());
    int max1 = 0;
    int max2 = 0;
    for (EntityAnnotation ea: mapping1.getAnnotations()) {
      if (ea.getSimVector() != null) {
        max1 = ea.getSimVector().length;
      }
    }
    if (max1 != 0) {
      max1 = 1;
    }
    for (EntityAnnotation ea: mapping2.getAnnotations()) {
      if (ea.getSimVector() != null) {
        max2 = ea.getSimVector().length;
      }
    }
    if (max2 != 0) {
      max2 = 1;
    }
    for (EntityAnnotation ea : mapping1.getAnnotations()) {
      float[] combinedVec = new float[max1+max2];
      float[] vec1 = ea.getSimVector();
      if (vec1 == null) {
        combinedVec[0] = ea.getSim();
      }else {
        for (int i = 0; i < max1; i++) {
          combinedVec[i] = ea.getSimVector()[i];
        }
      }
      if (mapping2.contains(ea)) {
        List<Float> sims = new ArrayList<>();
        sims.add(ea.getSim());
        sims.add(mapping2.getAnnotation(ea.getId()).getSim());
        EntityAnnotation ea2 = mapping2.getAnnotation(ea.getId());
        float[] vec2 = ea2.getSimVector();
        if (vec2 == null) {
          combinedVec[0] = ea2.getSim();
        }else {
          for (int i = 0; i < max2; i++) {
            combinedVec[i+max1] = ea.getSimVector()[i];
          }
        }
        float aggSim = func.aggregateFloatList(sims);
        EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                ea.getSrcAccession(), ea.getTargetAccession(), aggSim, ea.isVerfified());
        copy.setSimVector(combinedVec);
        unionMapping.addAnnotation(copy);
      }
    }
    return unionMapping;
  }


  public static AnnotationMapping diffWithVector(
          AnnotationMapping mapping1, AnnotationMapping mapping2) {
    AnnotationMapping unionMapping = new AnnotationMapping(mapping1.getSrcVersion(), mapping1.getTargetVersion());
    for (EntityAnnotation ea : mapping1.getAnnotations()) {
      if (!mapping2.contains(ea)) {
        EntityAnnotation copy = new EntityAnnotation(ea.getSrcId(), ea.getTargetId(),
                ea.getSrcAccession(), ea.getTargetAccession(), ea.getSim(), ea.isVerfified());
        unionMapping.addAnnotation(copy);
        copy.setSimVector(ea.getSimVector());
      }
    }
    return unionMapping;
  }


  /**
   * add all annotations to an unified annotation mapping that occur at least k times in the list of mappings
   *
   * @param mappings
   * @param support  ratio of occurrences with respect to the total number
   * @return
   */
  public static AnnotationMapping unionByOccurrences(List<AnnotationMapping> mappings, float support) {
    AnnotationMapping am = new AnnotationMapping(mappings.get(0).getSrcVersion(), mappings.get(0).getTargetVersion());
    am.setMethod("unified");
    am.setName(mappings.get(0).getName());
    Long2IntMap counter = new Long2IntOpenHashMap();
    int minSupport = Math.round(support * mappings.size());
    Long2FloatMap avgMap = new Long2FloatOpenHashMap();
    for (AnnotationMapping toolMapping : mappings) {
      for (EntityAnnotation ea : toolMapping.getAnnotations()) {
        if (!counter.containsKey(ea.getId())) {
          counter.put(ea.getId(), 1);
          avgMap.put(ea.getId(), ea.getSim());
        } else {
          counter.put(ea.getId(), counter.get(ea.getId()) + 1);
          avgMap.put(ea.getId(), ea.getSim() + avgMap.get(ea.getId()));
        }
        if (counter.get(ea.getId()) >= minSupport) {
          am.addAnnotation(ea);
        }
      }
      for (EntityAnnotation ea : am.getAnnotations()) {
        ea.setSim(ea.getSim() / (float) counter.get(ea.getId()));
      }
    }
    return am;
  }




}
