package de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.MLModel;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.Training;
import org.apache.log4j.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;

import java.util.Random;

/**
 * Created by christen on 08.08.2017.
 */
public class DecisionTree implements Training {


  float minC;

  float maxC;

  float stepC;

  int minM;

  int maxM;

  int stepM;

  float c;

  int m;

  double fmeasure = 0;

  double globalCorrect = 0;

  Logger log = Logger.getLogger(getClass());

  public DecisionTree() {
    this.minC = 0.1f;
    this.maxC =0.5f;
    this.stepC = 0.05f;
    this.minM = 2;
    this.maxM = 20;
    this.stepM =1;

  }

  @Override
  public MLModel train(Instances trainingInstances, String function) throws Exception {
    this.minC = 0.1f;
    this.maxC =0.5f;
    this.stepC = 0.05f;
    this.minM = 1;
    this.maxM = 20;
    this.stepM =1;
    J48 tree = new J48();
    int fold = 10;
    if (trainingInstances.numInstances()==1) {
      fold = 1;
    }else if (trainingInstances.numInstances()<30) {
      fold = 2;
    }else if (trainingInstances.numInstances()<=50) {
      fold = 5;
    }
    MLModel model;
    if (trainingInstances.numInstances()> 2) {
      Classifier cls = crossValidation(fold, trainingInstances, tree);
      model = new MLModel();
      model.setClassifier(cls);
      model.setFmeasure(fmeasure);
      model.setCorrectClasses(globalCorrect);
    }else {
      tree.setConfidenceFactor(0.3f);
      tree.setMinNumObj(1);
      tree.buildClassifier(trainingInstances);
      model = new MLModel();
      model.setClassifier(tree);
    }
    return model;
  }

  private J48 crossValidation(int folds, Instances data, J48 cls) throws Exception {
    int seed = 1;
    Random rand = new Random(seed);
    Instances randData = new Instances(data);
    randData.randomize(rand);
    if (randData.classAttribute().isNominal())
      randData.stratify(folds);
    double correct =0;
    fmeasure = 0;
    if (minM > data.numInstances()) {
      minM = data.numInstances();
    }
    if (maxM > data.numInstances()) {
      maxM = data.numInstances();
    }

//    CVParameterSelection cvParameterSelection = new CVParameterSelection();
//    cvParameterSelection.setClassifier(cls);
//    cvParameterSelection.setNumFolds(folds);
//    cvParameterSelection.buildClassifier(data);
    for (float cTemp=minC; cTemp<=maxC; cTemp+=stepC) {
      for (int instancesCount = minM; instancesCount <= maxM; instancesCount+=stepM) {
        Evaluation eval = new Evaluation(randData);
        cls.setConfidenceFactor(cTemp);
        cls.setMinNumObj(instancesCount);
        eval.crossValidateModel(cls, randData, folds, rand);
        if ((eval.fMeasure(0)*0.5+0.5*eval.fMeasure(1))>correct) {
        //if ((eval.correct())>correct) {
          correct = eval.fMeasure(0)*0.5+0.5*eval.fMeasure(1);
          fmeasure = eval.fMeasure(0);
          this.globalCorrect = eval.correct();
          m = instancesCount;
          c = cTemp;
          System.out.println("=== Setup c= " + (cTemp) + " ===");
          //System.out.println("ROC:"+eval.areaUnderROC(0));
          System.out.println("fmeasure:"+eval.fMeasure(0));
          System.out.println("fmeasure:"+eval.fMeasure(1));
          System.out.println("Classifier: " + cls.getClass().getName() + " " + Utils.joinOptions(cls.getOptions()));
          System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation run " + (cTemp + 1) + "===", false));
        }
      }
    }
    cls.setConfidenceFactor(c);
    cls.setMinNumObj(m);
    cls.buildClassifier(data);
    return cls;
  }
}
