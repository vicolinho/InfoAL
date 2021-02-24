package de.uni_leipzig.dbs.entity_resolution.examples.machineLearning.active;

import de.uni_leipzig.dbs.entity_resolution.evaluation.CSVResultWriter;
import de.uni_leipzig.dbs.entity_resolution.evaluation.DatabaseResultWriter;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.MLModel;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.ModelType;
import de.uni_leipzig.dbs.entity_resolution.data_model.SimilarityVector;
import de.uni_leipzig.dbs.entity_resolution.evaluation.EvaluationResult;
import de.uni_leipzig.dbs.entity_resolution.evaluation.MappingEvaluation;
import de.uni_leipzig.dbs.entity_resolution.examples.datasets.DataSource;
import de.uni_leipzig.dbs.entity_resolution.examples.datasets.DatasetLoader;
import de.uni_leipzig.dbs.entity_resolution.examples.datasets.SimVectorLoader;
import de.uni_leipzig.dbs.entity_resolution.examples.datasets.TrainingInstance2Instances;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.Oracle;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.sampling.*;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.ActiveLearningIteration;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.classification.BaselineClassification;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.feature.clustering.DistanceClustering;
import de.uni_leipzig.dbs.entity_resolution.util.AggregationFunction;
import de.uni_leipzig.dbs.entity_resolution.util.SetAnnotationOperator;
import de.uni_leipzig.dbs.formRepository.dataModel.AnnotationMapping;
import de.uni_leipzig.dbs.formRepository.dataModel.EntityAnnotation;
import de.uni_leipzig.dbs.formRepository.dataModel.util.CantorDecoder;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.apache.commons.cli.*;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by christen on 08.06.2018.
 */
public class InformativenessActiveLearning {

  private static ModelType classifierType = ModelType.TREE;

  private static DataSource dataSource = DataSource.CORA;

  private static int filterIndex = 0;

  private static double threshold = 0;


  private static int totalBudget = 1000;

  static int iterationSize = 40;


  private static long currentTime;

  private static SamplingType initialSelection = SamplingType.Farthest_First;

  //CORA
//  static int kClustering = 11;

  //BRAINZ
  static int kClustering = 23;

  //GS
//  static int kClustering = 14;



  static double alpha = -1;

  static Map<Integer, Long> times;

  static Options options;


  private static Map<Long, AnnotationMapping> globalGoldStandardMapping;

  private static Map<Long, Long2ObjectMap<SimilarityVector>> simVectors;
  private static String initialMethod;

  static {
    options = new Options();
    Option inputSimVectorFileOpt = new Option(ConsoleConstants.INPUT_SIMILARITY_FILE,
            "similarity vector file", true, "input similarity vector file");
    Option goldStandardOpt = new Option(ConsoleConstants.GOLD_STANDARD_FILE, "gold standard", true,
            "gold standard file for evaluation");
    Option initialSelectionMethodOpt = new Option(ConsoleConstants.INITIAL_SELECTION, "initial selection", true,
            "initial selection method for similarity vectors");
    Option budgetOpt = new Option(ConsoleConstants.BUDGET, "budget", true,
            "total number of selected pairs");

    Option iterationSizeOption = new Option(ConsoleConstants.ITERATION_SIZE, "iteration size", true,
            "number of selected vectors per iteration");

    Option alphaOption = new Option(ConsoleConstants.ALPHA, "iteration size", true,
            "number of selected vectors per iteration");
    Option dbHost = new Option(ConsoleConstants.DB_HOST, "host", true, "db host for evaluation result");
    dbHost.setOptionalArg(true);
    Option dbUser = new  Option(ConsoleConstants.USER, "db user", true, "db user");
    dbUser.setOptionalArg(true);
    Option dbPassword = new  Option(ConsoleConstants.PASSWORD, "db password", true, "db password");
    dbPassword.setOptionalArg(true);
    Option evalPath = new  Option(ConsoleConstants.EVALUATION_RESULT_FILE, "evaluation result file", true,
            "evaluation result file path");
    evalPath.setOptionalArg(true);
    options.addOption(inputSimVectorFileOpt);
    options.addOption(goldStandardOpt);
    options.addOption(initialSelectionMethodOpt);
    options.addOption(budgetOpt);
    options.addOption(iterationSizeOption);
    options.addOption(alphaOption);
    options.addOption(dbHost);
    options.addOption(dbUser);
    options.addOption(dbPassword);
    options.addOption(evalPath);

  }

  public static void main(String[] args) throws Exception {
    CommandLine cmd = parseCommand(args);
    alpha = Double.parseDouble(cmd.getOptionValue(ConsoleConstants.ALPHA));
    totalBudget = Integer.parseInt(cmd.getOptionValue(ConsoleConstants.BUDGET));
    iterationSize = Integer.parseInt(cmd.getOptionValue(ConsoleConstants.ITERATION_SIZE));
    initialMethod = cmd.getOptionValue(ConsoleConstants.INITIAL_SELECTION);
    String inputSimFile = cmd.getOptionValue(ConsoleConstants.INPUT_SIMILARITY_FILE);
    String inputGoldFile = cmd.getOptionValue(ConsoleConstants.GOLD_STANDARD_FILE);
    Random random = new Random(3863);
    currentTime = System.currentTimeMillis();
    times = new HashMap<>();
    DatasetLoader loader = new DatasetLoader();
    SimVectorLoader simVectorLoader = new SimVectorLoader();
      //0.05, 0.1, 0.2
    globalGoldStandardMapping = loader.getReferenceMappingForEvaluation(inputGoldFile);
    simVectors = simVectorLoader.loadSimVectors(inputSimFile,
            threshold, filterIndex);
    Map<Long, Classifier> classifierForSources = new HashMap<>();
    TrainingInstance2Instances transformation = new TrainingInstance2Instances();
    classifierForSources.clear();
    long sourcePairId = CantorDecoder.code(1, 2);
    LongSet tps = new LongOpenHashSet();
    for (EntityAnnotation ea : globalGoldStandardMapping.get(sourcePairId).getAnnotations()) {
      if (simVectors.get(sourcePairId).containsKey(ea.getId()))
        tps.add(ea.getId());
    }
    System.out.println("gold:" + tps.size());
    /*should be changed for real-world use case*/
    Oracle.getInstance().initialize(tps);

    Long2ObjectMap<SimilarityVector> vectors = simVectors.get(sourcePairId);
    Instances dataset = transformation.createInstances(vectors);
    Long2ObjectMap<Instance> linkInstanceMap = transformation.getLink2InstanceMap();
    int currentSampleDraw;
    LongSet initialSample = getInitialSample(random, linkInstanceMap, dataset,
            iterationSize, initialMethod);
    ActiveLearningIteration iterationOneModel = new ActiveLearningIteration(iterationSize,
            linkInstanceMap, classifierType, initialSample, alpha);
    iterationOneModel.setBudget(totalBudget);
    currentSampleDraw = iterationSize;
    do {
      LongSet simVectorPairIds = iterationOneModel.iteration();
      Long2IntMap classifiedVectors = Oracle.getInstance().classifySimVectorPairs(simVectorPairIds);
      iterationOneModel.updateTrainingData(classifiedVectors);
      currentSampleDraw += iterationOneModel.getSelectedNumberForIteration();
      System.gc();
    } while (currentSampleDraw < totalBudget && iterationOneModel.getSelectedNumberForIteration() != 0);
    MLModel model = iterationOneModel.getClassificationModel();
    classifierForSources.put(sourcePairId, model.getClassifier());

    EvaluationResult er = evaluateClassifier(classifierForSources, globalGoldStandardMapping, simVectors);

    writeResult(cmd.getOptionValue(ConsoleConstants.DB_HOST), cmd.getOptionValue(ConsoleConstants.USER),
            cmd.getOptionValue(ConsoleConstants.PASSWORD),
            cmd.getOptionValue(ConsoleConstants.EVALUATION_RESULT_FILE), er);
  }

  private static void writeResult(String dbUrl, String user, String pw, String csvFile,
                                  EvaluationResult result) throws IOException, SQLException {
    if (csvFile != null) {
      CSVResultWriter.writeResult(csvFile, initialMethod, iterationSize, totalBudget,
              result.getMeasures().get("precision"), result.getMeasures().get("recall"),
              result.getMeasures().get("fmeasure"), alpha, true);
    }
    if (dbUrl != null && user != null && pw != null) {
      DatabaseResultWriter dbWriter = DatabaseResultWriter.getInstance(dataSource);
      dbWriter.openConnection(dbUrl, user, pw);
      dbWriter.writeEvaluationResult(currentTime,
              totalBudget,
              iterationSize,
              initialMethod,
              classifierType,
              (double) result.getMeasures().get("precision"),
              (double) result.getMeasures().get("recall"),
              (double) result.getMeasures().get("fmeasure"), alpha, 0);
    }
  }


  private static CommandLine parseCommand(String[] args) {
    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("utility-name", options);
      System.exit(1);
    }
    return cmd;
  }

  /**
   * write evaluate the generated model based on the selected similarity vectors with the gold standard and
   * write the result to a DB or CSV
   * @param classifiers
   * @param goldStandardForAllSources
   * @param simVectors
   * @throws Exception
   */
  private static EvaluationResult evaluateClassifier(Map<Long, Classifier> classifiers,
                   Map<Long, AnnotationMapping> goldStandardForAllSources,
                   Map<Long, Long2ObjectMap<SimilarityVector>> simVectors) throws Exception{
    BaselineClassification baselineClassification = new BaselineClassification();
    AnnotationMapping globalGoldStandard = new AnnotationMapping();
    AnnotationMapping globalPredictedMapping = new AnnotationMapping();
    long sourcePairId = CantorDecoder.code(1, 2);
    globalGoldStandard = SetAnnotationOperator.union(AggregationFunction.MAX, globalGoldStandard,
            goldStandardForAllSources.get(sourcePairId));
    AnnotationMapping am = baselineClassification.predictLinks(simVectors.get(sourcePairId),
            classifiers.get(sourcePairId));
    globalPredictedMapping = SetAnnotationOperator.union(AggregationFunction.MAX, globalPredictedMapping, am);
    System.out.println(globalGoldStandard.getNumberOfAnnotations());
    System.out.println("prediction"+globalPredictedMapping.getNumberOfAnnotations());

    MappingEvaluation me = new MappingEvaluation();
    EvaluationResult er = me.getResult(globalPredictedMapping, globalGoldStandard, "test", "test");
    return er;
  }

  /**
   * selects a set of initial similarity vectors based on the inital selection strategy (Farthest First,
   * Stratified Sampling, Random)
   * @param r random instance with a certain seed
   * @param instances dictionary consisting of pair id and the corresponding instance
   *                  representing the similarity vector
   * @param dataset weka set of instances representing similarity vectors
   * @param sampleSize number of selected vectors
   * @return set of ids representing similarity vector that are selected at the beginning
   */
  private static LongSet getInitialSample(Random r, Long2ObjectMap<Instance> instances,
                                          Instances dataset, int sampleSize, String method) {

    Sampling sampling;

    if (method.equals(SamplingType.Stratified_Sampling.name())) {
      sampling = new StratifiedSampling.Builder()
              .distance(DistanceClustering.EUCLID)
              .k(kClustering)
              .build();
    } else if (method.equals(SamplingType.Farthest_First.name())){
      sampling = new FarthestFirst();
    } else {
      sampling = new RandomSampling();
    }
    return sampling.getSample(r, instances, dataset, sampleSize);
  }
}
