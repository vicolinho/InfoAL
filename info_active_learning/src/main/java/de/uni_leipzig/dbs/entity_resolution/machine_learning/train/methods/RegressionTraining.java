package de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.MLModel;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.Training;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.*;

import java.util.*;

/**
 * Created by christen on 13.06.2017.
 */
public class RegressionTraining implements Training {



  float minRE;

  float maxRE;

  float stepRE;

  int minM;

  int maxM;

  int stepM;

  float c;

  int m;

  public RegressionTraining() {
    this.minRE = 4f;
    this.maxRE =4f;
    this.stepRE = 2f;
    this.minM = -1;
    this.maxM = -1;
    this.stepM =1;
  }


  @Override
  public MLModel train(Instances trainingInstances, String function) throws Exception {
    Logistic tree = new Logistic();
    int fold = 10;
    if (trainingInstances.numInstances() < 10) {
      fold = 2;
    }
    Classifier cls = crossValidation(fold, trainingInstances, tree);
    MLModel model = new MLModel();
    model.setClassifier(cls);
    return model;
  }

  private Logistic crossValidation(int folds, Instances data, Logistic cls) throws Exception {
    int seed = 1;
    Random rand = new Random(seed);
    Instances randData = new Instances(data);
    randData.randomize(rand);
    if (randData.classAttribute().isNominal())
      randData.stratify(folds);
    double correct = 0;
    for (float cTemp = minRE; cTemp <= maxRE; cTemp += stepRE) {
      for (int instancesCount = minM; instancesCount <= maxM; instancesCount += stepM) {
        Evaluation eval = new Evaluation(randData);
        double ridge = Math.pow(2, cTemp);
        cls.setRidge(ridge);
        eval.crossValidateModel(cls, randData, folds, rand);
//        for (int n = 0; n < folds; n++) {
//          Instances train = randData.trainCV(folds, n,rand);
//          eval.setPriors(train);
//          Instances test = randData.testCV(folds, n);
//          Classifier clsCopy = SMO.makeCopy(cls);
//          clsCopy.buildClassifier(train);
//          eval.evaluateModel(clsCopy, test);
//        }
        if (eval.correct() > correct) {
          correct = eval.correct();
          m = instancesCount;
          c = cTemp;
          System.out.println("=== Setup c= " + (cTemp) + " ===");
          System.out.println("fmeasure:" + eval.fMeasure(0));
          System.out.println("fmeasure:" + eval.fMeasure(1));
          System.out.println("Classifier: " + cls.getClass().getName() + " " + Utils.joinOptions(cls.getOptions()));
          System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation run " + (cTemp + 1) + "===", false));
        }
      }
    }
//    cls.setRidge(c);
    cls.buildClassifier(data);
    return cls;
  }
}
