package de.uni_leipzig.dbs.entity_resolution.io.training;

import de.uni_leipzig.dbs.entity_resolution.constants.TrainingHeader;
import de.uni_leipzig.dbs.entity_resolution.data_model.SimilarityVector;
import de.uni_leipzig.dbs.formRepository.dataModel.AnnotationMapping;
import de.uni_leipzig.dbs.formRepository.dataModel.EntityAnnotation;
import de.uni_leipzig.dbs.formRepository.dataModel.util.CantorDecoder;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christen on 26.09.2017.
 */
public class TrainingVectorReader {


  /**
   * true positive vectors
   */
  AnnotationMapping tpsForVectors;

  public Long2ObjectMap<SimilarityVector> readTrainingData(String file) throws IOException {
    tpsForVectors = new AnnotationMapping();
    Long2ObjectMap<SimilarityVector> instances = new Long2ObjectOpenHashMap<>();
    BufferedReader br = new BufferedReader(new FileReader(file));
    boolean firstLine = true;
    Map<String, Integer> fieldDict = new HashMap<>();
    IntList indicesSim = new IntArrayList();
    int lines = 0;
    while (br.ready()) {
      String line = br.readLine();
      String[] dataValues = line.split("(;|\\,)");
      if (firstLine) {
        for (int i =0; i< dataValues.length; i++) {
          fieldDict.put(dataValues[i], i);
          if (!dataValues[i].equals(TrainingHeader.SRC_ID) &&
                  !dataValues[i].equals(TrainingHeader.TARGET_ID) &&
                  !dataValues[i].equals(TrainingHeader.VERIFIED)) {
            indicesSim.add(i);
          }
        }
        firstLine = false;
      } else {
        int srcId = Integer.parseInt(dataValues[fieldDict.get(TrainingHeader.SRC_ID)]);
        int targetId = Integer.parseInt(dataValues[fieldDict.get(TrainingHeader.TARGET_ID)]);
        boolean verified = Boolean.parseBoolean(dataValues[fieldDict.get(TrainingHeader.VERIFIED)]);
        if (!verified) {
          try {
            verified = (Double.parseDouble(dataValues[fieldDict.get(TrainingHeader.VERIFIED)])==1d);
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
        }
        DoubleList simVec = new DoubleArrayList();
        for (int simIndex : indicesSim) {
          simVec.add(Double.parseDouble(dataValues[simIndex]));
        }
        SimilarityVector ti = new SimilarityVector(srcId, targetId, verified, simVec.size());
        if (verified) {
          tpsForVectors.addAnnotation(new EntityAnnotation(srcId, targetId, null, null, 1, true));
        }
        ti.setSimVector(simVec.toArray(new double[]{}));
        if (!instances.containsKey(CantorDecoder.code(srcId, targetId))) {
          instances.put(CantorDecoder.code(srcId, targetId), ti);
        }else {
          System.out.println("already contained"+ srcId + ", " + targetId);
          SimilarityVector i = instances.get(CantorDecoder.code(srcId, targetId));
          System.out.println(i.getSrcId() + "  " + i.getTargetId());
        }
      }
    }
    return instances;
  }

  public Long2ObjectMap<SimilarityVector> readTrainingData(String file, int[] usedIndices) throws IOException {
    tpsForVectors = new AnnotationMapping();
    Long2ObjectMap<SimilarityVector> instances = new Long2ObjectOpenHashMap<>();
    BufferedReader br = new BufferedReader(new FileReader(file));
    boolean firstLine = true;
    Map<String, Integer> fieldDict = new HashMap<>();
    IntList indicesSim = new IntArrayList();
    IntSet used = new IntOpenHashSet(usedIndices);
    while (br.ready()) {
      String line = br.readLine();
      String[] dataValues = line.split("(;|\\,)");
      if (firstLine) {
        for (int i =0; i< dataValues.length; i++) {
          fieldDict.put(dataValues[i], i);
          if (!dataValues[i].equals(TrainingHeader.SRC_ID) &&
                  !dataValues[i].equals(TrainingHeader.TARGET_ID) &&
                  !dataValues[i].equals(TrainingHeader.VERIFIED)) {
            if(used.contains(i)) {
              indicesSim.add(i);
            }
          }
        }
        firstLine = false;
      }else {
        int srcId = Integer.parseInt(dataValues[fieldDict.get(TrainingHeader.SRC_ID)]);
        int targetId = Integer.parseInt(dataValues[fieldDict.get(TrainingHeader.TARGET_ID)]);
        boolean verified = Boolean. parseBoolean(dataValues[fieldDict.get(TrainingHeader.VERIFIED)]);
        if (!verified) {
          try {
            verified = (Double.parseDouble(dataValues[fieldDict.get(TrainingHeader.VERIFIED)])==1d);
          }catch(NumberFormatException e){}
        }
        DoubleList simVec = new DoubleArrayList();
        for (int simIndex : indicesSim) {
          simVec.add(Double.parseDouble(dataValues[simIndex]));
        }
        SimilarityVector ti = new SimilarityVector(srcId, targetId, verified, simVec.size());
        if (verified) {
          tpsForVectors.addAnnotation(new EntityAnnotation(srcId, targetId, null, null, 1, true));
        }
        ti.setSimVector(simVec.toArray(new double[]{}));
        instances.put(CantorDecoder.code(srcId, targetId), ti);
      }
    }
    return instances;
  }

  public AnnotationMapping getTpsForVectors() {
    return tpsForVectors;
  }
}
