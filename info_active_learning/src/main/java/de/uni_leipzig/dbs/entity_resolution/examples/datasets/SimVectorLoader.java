package de.uni_leipzig.dbs.entity_resolution.examples.datasets;

import de.uni_leipzig.dbs.entity_resolution.data_model.SimilarityVector;
import de.uni_leipzig.dbs.entity_resolution.io.training.TrainingVectorReader;
import de.uni_leipzig.dbs.formRepository.dataModel.AnnotationMapping;
import de.uni_leipzig.dbs.formRepository.dataModel.EntityStructureVersion;
import de.uni_leipzig.dbs.formRepository.dataModel.util.CantorDecoder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class loads similarity vector files for precalculated vector for a certain data source {Music, GS, CORA}
 */
public class SimVectorLoader {

  public static final String TRAINING_PATH_MUSIC = "E:/data/musicbrainz/training_data/";

  public static final String TRAINING_PATH_BIB = "E:/data/publication/DBLP-Scholar/training_data/";

  public static final String TRAINING_PATH_PRODUCT = "E:/data/products/training_data/";


  public static final String MAPPING_AMAZON_PRODUCT = "E:/data/weightvectors/amazon-googleproducts-weight-vectors.csv/" +
          "amazon-googleproducts-weight-vectors_mod.csv";

  public static final String MAPPING_BIB_2 = "E:/data/weightvectors/dblp-gs-weight-vectors.csv/dblp-gs-weight-vectors.csv";

//  public static final String MAPPING_CORA = "E:/data/weightvectors/cora-weight-vectors-full.csv/cora-weight-vectors-full_mod.csv";

  public static final String MAPPING_CORA = "E:/data/weightvectors/cora-weight-vectors.csv/cora-weight-vectors.csv";

  public static final String MAPPING_MUSIC = "E:/data/weightvectors/musicbrainz-weight-vectors.csv/musicbrainz-weight-vectors.csv";

  public static final String MAPPING_NCVOTER = "E:/data/weightvectors/ncvr-weight-vectors.csv/ncvr-weight-vectors_mod.csv";


  Map<Long, AnnotationMapping> tpsForVectors;

  Logger log = Logger.getLogger(getClass());

  public Map<Long, Long2ObjectMap<SimilarityVector>> loadSimVectors(DataSource dataSource,
                                                                    Map<Integer, EntityStructureVersion> sources) throws IOException {
    tpsForVectors = new HashMap<>();
    Map<Long, Long2ObjectMap<SimilarityVector>> trainingData = new Long2ObjectOpenHashMap<>();

    List<EntityStructureVersion> sourceList = new ArrayList<>(sources.values());
    String basicPath = "";
    switch (dataSource) {
      case MUSIC:
        basicPath = TRAINING_PATH_MUSIC;
        break;
      case BIB:
        basicPath = TRAINING_PATH_BIB;
        break;
      case PRODUCT:
        basicPath = TRAINING_PATH_PRODUCT;
      break;
    }
    TrainingVectorReader tvr = new TrainingVectorReader();
    for (int i = 0; i < sourceList.size(); i++) {
      for (int k = i+1; k< sourceList.size(); k++) {
        String path = basicPath+
                "training_["+sourceList.get(i).getStructureId()+"_"+sourceList.get(k).getStructureId()+".csv";
        Long2ObjectMap<SimilarityVector> trainInstances = tvr.readTrainingData(path);
        long sourcePairId = CantorDecoder.code(sourceList.get(i).getStructureId(), sourceList.get(k).getStructureId());
        tpsForVectors.put(sourcePairId, tvr.getTpsForVectors());
        trainingData.put(sourcePairId, trainInstances);
      }
    }
    return trainingData;
  }

  public Map<Long, Long2ObjectMap<SimilarityVector>> loadSimVectors(DataSource dataSource,
                                                                    Map<Integer, EntityStructureVersion> sources, double threshold, int index) throws IOException {
    tpsForVectors = new HashMap<>();
    Map<Long, Long2ObjectMap<SimilarityVector>> trainingData = new Long2ObjectOpenHashMap<>();

    List<EntityStructureVersion> sourceList = new ArrayList<>(sources.values());
    String basicPath = "";
    switch (dataSource) {
      case MUSIC:
        basicPath = TRAINING_PATH_MUSIC;
        break;
      case BIB:
        basicPath = TRAINING_PATH_BIB;
        break;
      case PRODUCT:
        basicPath = TRAINING_PATH_PRODUCT;
        break;

    }
    TrainingVectorReader tvr = new TrainingVectorReader();
    for (int i = 0; i < sourceList.size(); i++) {
      for (int k = i+1; k< sourceList.size(); k++) {
        String path = basicPath+
                "training_["+sourceList.get(i).getStructureId()+"_"+sourceList.get(k).getStructureId()+".csv";
        Long2ObjectMap<SimilarityVector> trainInstances = tvr.readTrainingData(path);
        for (long key: trainInstances.keySet()) {
          if (trainInstances.get(key).getSimVector()[index] < threshold) {
            trainInstances.remove(key);
          }
        }
        long sourcePairId = CantorDecoder.code(sourceList.get(i).getStructureId(), sourceList.get(k).getStructureId());
        tpsForVectors.put(sourcePairId, tvr.getTpsForVectors());
        trainingData.put(sourcePairId, trainInstances);
      }
    }
    return trainingData;
  }


  /**
   * load similarty vectors from file depending on the specified data source
   * @param dataSource
   * @param threshold
   * @param index
   * @return
   * @throws IOException
   */
  public Map<Long, Long2ObjectMap<SimilarityVector>> loadSimVectors(DataSource dataSource,
                                                                    double threshold, int index) throws IOException {
    tpsForVectors = new HashMap<>();
    Map<Long, Long2ObjectMap<SimilarityVector>> trainingData = new Long2ObjectOpenHashMap<>();

    String path = "";
    switch (dataSource) {
      case BIB: path = MAPPING_BIB_2;
        break;
      case CORA: path = MAPPING_CORA;
        break;
      case AMAZON_GOOGLE: path = MAPPING_AMAZON_PRODUCT;
        break;
      case NCVOTER: path = MAPPING_NCVOTER;
        break;
      case MUSIC: path = MAPPING_MUSIC;
    }
    TrainingVectorReader tvr = new TrainingVectorReader();
    Long2ObjectMap<SimilarityVector> trainInstances = tvr.readTrainingData(path);
    log.info("#Train" + trainInstances.size());
    for (long key: trainInstances.keySet()) {
      if (trainInstances.get(key).getSimVector()[index] < threshold) {
        trainInstances.remove(key);
      }
    }
    log.info("filtered Train "+ trainInstances.size());
    long sourcePairId = CantorDecoder.code(1, 2);
    tpsForVectors.put(sourcePairId, tvr.getTpsForVectors());
    trainingData.put(sourcePairId, trainInstances);
    return trainingData;
  }

  public Map<Long, Long2ObjectMap<SimilarityVector>> loadSimVectors(String filePath,
                                                                    double threshold, int index) throws IOException {
    tpsForVectors = new HashMap<>();
    Map<Long, Long2ObjectMap<SimilarityVector>> trainingData = new Long2ObjectOpenHashMap<>();
    TrainingVectorReader tvr = new TrainingVectorReader();
    Long2ObjectMap<SimilarityVector> trainInstances = tvr.readTrainingData(filePath);
    for (long key: trainInstances.keySet()) {
      if (trainInstances.get(key).getSimVector()[index] < threshold) {
        trainInstances.remove(key);
      }
    }
    long sourcePairId = CantorDecoder.code(1, 2);
    tpsForVectors.put(sourcePairId, tvr.getTpsForVectors());
    trainingData.put(sourcePairId, trainInstances);
    return trainingData;
  }
}
