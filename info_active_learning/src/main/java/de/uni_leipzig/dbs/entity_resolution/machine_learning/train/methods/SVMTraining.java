package de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.MLModel;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.Training;
import org.apache.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.CachedKernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.*;

import java.util.*;

/**
 * Created by christen on 07.06.2017.
 */
public class SVMTraining implements Training {

  public static final String RBF= "rbf";

  public static final String POLY= "polynom";

  private static Map<String, CachedKernel> functions;

  private CachedKernel kernel;

  private double c;

  private double gamma;

  private int minCE;

  private int maxCE;

  private int stepC;

  private int minGammaE;

  private int maxGammaE;

  private int stepGamma;

  private int minEpsE;

  private int maxEpsE;

  private int stepEps;


  Logger log = Logger.getLogger(getClass());


  int threadNumber = 16;


  public SVMTraining (){


    functions = new HashMap<>();
    functions.put(RBF, new RBFKernel());
    functions.put(POLY, new PolyKernel());
    c = 1000;
    gamma = Math.pow(10,-12);
    this.minCE = -3;
    this.maxCE = 9;
    this.minGammaE = -9;
    this.maxGammaE = 5;
    stepGamma =2;
    stepC =1;
    minEpsE = -7;
    maxEpsE = 5;
    stepEps = 1;

  }

  public SVMTraining(double c, double eps){
    this.c = c;
    this.gamma = eps;
    functions = new HashMap<>();
    functions.put(RBF, new RBFKernel());
    functions.put(POLY, new PolyKernel());
    this.minCE = -5;
    this.maxCE = 8;
    stepC =1;
    this.minGammaE = -9;
    this.maxGammaE = 5;
    stepGamma = 1;
    minEpsE = -7;
    maxEpsE = 5;
    stepEps = 2;
  }

  @Override
  public MLModel train(Instances trainingInstances, String function) throws Exception {
    CachedKernel kernel = functions.get(function);
    SMO classifier= new SMO();
    this.kernel = kernel;
    classifier.setKernel(kernel);
    int fold = 5;
    if (trainingInstances.numInstances()<50) {
      fold = 5;
    }
    classifier = threadedcrossValidation(fold, trainingInstances, classifier);
    MLModel model = new MLModel();
    model.setClassifier(classifier);
    return model;

  }

  private SMO crossValidation(int folds, Instances data, SMO cls) throws Exception {
    int seed = 1;
    Random rand = new Random(seed);
    Instances randData = new Instances(data);
    randData.randomize(rand);
    if (randData.classAttribute().isNominal())
      randData.stratify(folds);
    double correct =0;
    for (int i=minCE; i<=maxCE; i+=stepC) {
      for (int de = minGammaE; de <= maxGammaE; de+= stepGamma) {
        double epsTemp = Math.pow(2, de);
        double cTemp = Math.pow(2, i);
        Evaluation eval = new Evaluation(randData);
        cls.setC(cTemp);
        if (cls.getKernel() instanceof RBFKernel){
          ((RBFKernel) cls.getKernel()).setGamma(de);
        }
        eval.crossValidateModel(cls, randData, folds, rand);
//        for (int n = 0; n < folds; n++) {
//          Instances train = randData.trainCV(folds, n,rand);
//          eval.setPriors(train);
//          Instances test = randData.testCV(folds, n);
//          Classifier clsCopy = SMO.makeCopy(cls);
//          clsCopy.buildClassifier(train);
//          eval.evaluateModel(clsCopy, test);
//        }
        if (eval.correct()>correct){
          correct = eval.correct();
          gamma = epsTemp;
          c = cTemp;
//          System.out.println("=== Setup c= " + (cTemp) + " ===");
//          System.out.println("fmeasure:"+eval.fMeasure(0));
//          System.out.println("fmeasure:"+eval.fMeasure(1));
//          System.out.println("Classifier: " + cls.getClass().getName() + " " + Utils.joinOptions(cls.getOptions()));
//          System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation run " + (i + 1) + "===", false));
        }
      }
    }
    cls.setC(c);
    if (kernel instanceof RBFKernel){
      ((RBFKernel) kernel).setGamma(gamma);
    }
    cls.setKernel(kernel);
    cls.buildClassifier(data);
    return cls;
  }

  private SMO threadedcrossValidation(int folds, Instances data, SMO cls) throws Exception {
    int seed = 1;
    Random rand = new Random(seed);
    Instances randData = new Instances(data);
    randData.randomize(rand);

    if (randData.classAttribute().isNominal())
      randData.stratify(folds);
    double correct =0;
    List<SVMParameters> parameters = new ArrayList<>();

    for (int i=minCE; i<=maxCE; i += stepC) {
//      for (double c : new double[]{0.1, 0.2, 0.5, 0.75, 1, 2, 5, 10}) {
      for (int de = minGammaE; de <= maxGammaE; de += stepGamma) {
        for (int epsE = minEpsE; epsE <= maxEpsE; epsE += stepEps) {
          double gammaTemp = Math.pow(2, de);
//          double gammaTemp = 1/(double) data.numAttributes();
          double cTemp = Math.pow(2, i);
//        double cTemp = c;
          double epsTemp = Math.pow(2, epsE);
//          double epsTemp = 1.0e-12;
          SVMParameters p = new SVMParameters(cTemp, gammaTemp, epsTemp);
          parameters.add(p);
        }
      }
    }
    System.out.println("number of params:"+parameters.size());
    List<List<SVMParameters>> paramsForThreads = new ArrayList<>();
    threadNumber = (parameters.size() < threadNumber) ? parameters.size() : threadNumber;
    for (int i = 0; i<threadNumber; i++){
      paramsForThreads.add(new ArrayList<>());
    }
    for (int i = 0; i< parameters.size(); i++){
      paramsForThreads.get(i%threadNumber).add(parameters.get(i));
    }
    List<SVMTrainingThread> threads = new ArrayList<>();
    for (int t = 0; t<threadNumber; t++){
      Instances copy = new Instances(randData);
      SVMTrainingThread thread = new SVMTrainingThread(copy, paramsForThreads.get(t), folds, cls);
      threads.add(thread);
    }
    for (int t = 0; t<threadNumber; t++){
      SVMTrainingThread thread = threads.get(t);
      thread.start();
    }
    for (int t = 0; t<threadNumber; t++){
      SVMTrainingThread thread = threads.get(t);
      thread.join();
    }

    double globalCorrect = 0;
    SVMParameters best = null;
    for (int t = 0; t<threadNumber; t++){
      SVMTrainingThread thread = threads.get(t);
      if (thread.getLocalMaxCorrect()>globalCorrect){
        globalCorrect = thread.getLocalMaxCorrect();
        best = thread.getBestParam();
      }
      thread.clear();
    }
    threads.clear();
    log.info(best);
    cls.setC(best.getC());
    cls.setEpsilon(best.getEps());
    if (kernel instanceof RBFKernel) {
      ((RBFKernel) kernel).setGamma(best.getGamma());
    }
    //cls.setEpsilon(best.getGamma());
    cls.setKernel(kernel);
    cls.buildClassifier(data);
    return cls;
  }
}
