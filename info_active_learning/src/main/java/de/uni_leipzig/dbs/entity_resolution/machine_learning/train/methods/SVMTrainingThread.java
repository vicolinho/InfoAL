package de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods;


import org.apache.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;
import weka.core.Utils;

import java.util.List;
import java.util.Random;

/**
 * Created by christen on 08.08.2017.
 */
public class SVMTrainingThread extends Thread{

  Logger log = Logger.getLogger(getClass());

  Instances instances;

  SMO cls;

  int folds;

  private List<SVMParameters> params;

  private double localMaxCorrect;

  private SVMParameters bestParam;

  public SVMTrainingThread(Instances instances, List<SVMParameters> params, int folds,
                           SMO cls) {
    super();
    this.instances = instances;
    this.folds = folds;
    this.params = params;
    try {
      this.cls = (SMO) SMO.makeCopy(cls);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    double correct =0;
    int seed = 5903;
    Random rand = new Random(seed);
    try {
      int processed = 0;
      for (SVMParameters p : params) {
        double gammaTemp = p.getGamma();
        double cTemp = p.getC();
        double epsTemp = p.getEps();
        Evaluation eval = new Evaluation(instances);
        cls.setC(cTemp);
        cls.setEpsilon(epsTemp);
        if (cls.getKernel() instanceof RBFKernel){
          ((RBFKernel) cls.getKernel()).setGamma(gammaTemp);
        }else if (cls.getKernel() instanceof PolyKernel) {
          ((PolyKernel)cls.getKernel()).setExponent(3);
        }

        eval.crossValidateModel(cls, instances, folds, rand);
        if ((eval.fMeasure(0)*0.6+0.4*eval.fMeasure(1))>correct) {
        //if (eval.fMeasure(0) > correct) {
          correct = (eval.fMeasure(0)*0.6+0.4*eval.fMeasure(1));
          bestParam = p;
          System.out.println("=== Setup c= " + (cTemp) + " ===");
          System.out.println("fmeasure:"+eval.fMeasure(0));
          System.out.println("not fmeasure:"+eval.fMeasure(1));
          System.out.println("Classifier: " + cls.getClass().getName() + " " + Utils.joinOptions(cls.getOptions()));
          System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation run " + (p.getC()) + "===", false));
        }
        processed++;
        if (processed%2 == 0){
          log.info(processed +" of "+params.size());
        }
      }
      localMaxCorrect = correct;

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public SVMParameters getBestParam() {
    return bestParam;
  }

  public double getLocalMaxCorrect() {
    return localMaxCorrect;
  }

  public void clear() {
    this.instances.clear();
    this.instances = null;
    this.params.clear();
  }
}
