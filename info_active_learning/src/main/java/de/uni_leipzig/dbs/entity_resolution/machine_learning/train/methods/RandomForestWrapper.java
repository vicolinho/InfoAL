package de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.MLModel;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.Training;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.Utils;

import java.util.Random;

/**
 * Created by christen on 18.01.2018.
 */
public class RandomForestWrapper implements Training{


  int minTree =10;
  int maxTree = 100;
  int  minDepth = 2;
  int maxDepth = 20;
  float fmeasure;
  double globalCorrect;
  int optimalNumber;
  int optimalDepth;

  @Override
  public MLModel train(Instances trainingInstances, String function) throws Exception {
    RandomForest tree = new RandomForest();
    int fold = 5;
    if (trainingInstances.numInstances()<50) {
      fold = 5;
    }
    Classifier cls = crossValidation(fold, trainingInstances, tree);
    MLModel model = new MLModel();
    model.setClassifier(cls);
    return model;
  }

  private RandomForest crossValidation(int folds, Instances data, RandomForest cls) throws Exception {
    int seed = 1;
    Random rand = new Random(seed);
    Instances randData = new Instances(data);
    randData.randomize(rand);
    if (randData.classAttribute().isNominal())
      randData.stratify(folds);
    double correct =0;
    fmeasure = 0;
    cls.setNumExecutionSlots(16);
    cls.setMaxDepth(0);
    cls.setBreakTiesRandomly(false);
//    if (maxM > data.numInstances()) {
//      maxM = data.numInstances();
//    }
    for (int number=minTree; number<=maxTree; number+=5) {
      for (int depth = minDepth; depth <= maxDepth; depth+=1) {
        Evaluation eval = new Evaluation(randData);
        cls.setMaxDepth(depth);
        cls.setNumIterations(number);
        eval.crossValidateModel(cls, randData, folds, rand);
//        for (int n = 0; n < folds; n++) {
//          Instances train = randData.trainCV(folds, n,rand);
//          eval.setPriors(train);
//          Instances test = randData.testCV(folds, n);
//          Classifier clsCopy = SMO.makeCopy(cls);
//          clsCopy.buildClassifier(train);
//          eval.evaluateModel(clsCopy, test);
//        }

        if ((eval.fMeasure(0)*0.6+0.4*eval.fMeasure(1))>correct) {
          correct = (eval.fMeasure(0)*0.6+0.4*eval.fMeasure(1));
          this.globalCorrect = eval.correct();
          optimalNumber = number;
          optimalDepth = depth;
          System.out.println("fmeasure match:"+eval.fMeasure(0));
          System.out.println("fmeasure non-match:"+eval.fMeasure(1));
          System.out.println("Classifier: " + cls.getClass().getName() + " " + Utils.joinOptions(cls.getOptions()));
          System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation run " + (minTree + 1) + "===", false));
        }
      }
    }
    cls.setMaxDepth(optimalNumber);
    cls.setNumIterations(optimalDepth);
    cls.buildClassifier(data);
    return cls;
  }
}
