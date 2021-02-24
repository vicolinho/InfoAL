package de.uni_leipzig.dbs.entity_resolution.util;

/**
 * Created by christen on 14.06.2018.
 */
public class CosineSimComputation {

  public static double sim(double[] vec1, double[] vec2) {
    double vecProd = 0;
    double lengthFi = 0;
    double length = 0;
    for (int i = 0; i < vec1.length; i++) {
      vecProd += vec1[i]*vec2[i];
      lengthFi += vec1[i]*vec1[i];
      length += vec2[i] * vec2[i];
    }
    if (lengthFi == 0 || length == 0) {
      //log.info(Arrays.toString(centroid));
      return 0;
    }
    double cosineSim = vecProd/(Math.sqrt(lengthFi)*Math.sqrt(length));
    if (cosineSim < 0.00000001){
      //log.info("f:"+fi.toString());
      //log.info(Arrays.toString(centroid));
      return 0;
    }
    return cosineSim;
  }

  public static double euclideanDistance(double[] vec1, double[] vec2) {
    double distance = 0;
    for (int i= 0; i<vec1.length; i++) {
      distance+=((vec2[i] - vec1[i])*(vec2[i] - vec1[i]));
    }
    distance = Math.sqrt(distance);
    return distance;
  }
}
