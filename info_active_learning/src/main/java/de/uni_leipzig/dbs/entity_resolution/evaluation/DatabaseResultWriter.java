package de.uni_leipzig.dbs.entity_resolution.evaluation;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.ModelType;
import de.uni_leipzig.dbs.entity_resolution.examples.datasets.DataSource;

import java.sql.*;

/**
 * Created by christen on 10.10.2017.
 */
public class DatabaseResultWriter {

  static Connection con;

  private static DatabaseResultWriter self;

  PreparedStatement stmt;


  private DatabaseResultWriter(DataSource source){
    try {
      switch (source) {
        case MUSIC: stmt = con.prepareStatement("Insert Into music_evaluation(feature, cluster_method, training_ratio, cluster_size, " +
                "method, classifier, prec, recall, fmeasure, tpRatio, result_time, kcentroid)" +
                "Values(?,?,?,?,?,?,?,?,?,?,?,?)");
          break;
        case PRODUCT:
          stmt = con.prepareStatement("Insert Into product_evaluation(feature, cluster_method, training_ratio, cluster_size, " +
                  "method, classifier, prec, recall, fmeasure, tpRatio, result_time, kcentroid)" +
                  "Values(?,?,?,?,?,?,?,?,?,?,?,?)");
          break;
        case BIB:
          stmt = con.prepareStatement("Insert Into bib_evaluation(feature, cluster_method, training_ratio, cluster_size, " +
                  "method, classifier, prec, recall, fmeasure, tpRatio, result_time, kcentroid)" +
                  "Values(?,?,?,?,?,?,?,?,?,?,?,?)");
          break;
        case CORA:
          stmt = con.prepareStatement("Insert Into cora_evaluation(feature, cluster_method, training_ratio, cluster_size, " +
                  "method, classifier, prec, recall, fmeasure, tpRatio, result_time, kcentroid)" +
                  "Values(?,?,?,?,?,?,?,?,?,?,?,?)");
          break;
        case NCVOTER:
          stmt = con.prepareStatement("Insert Into ncvr_evaluation(feature, cluster_method, training_ratio, cluster_size, " +
                  "method, classifier, prec, recall, fmeasure, tpRatio, result_time, kcentroid)" +
                  "Values(?,?,?,?,?,?,?,?,?,?,?,?)");
          break;
        case AMAZON_GOOGLE:
          stmt = con.prepareStatement("Insert Into amazon_google(feature, cluster_method, training_ratio, cluster_size, " +
                  "method, classifier, prec, recall, fmeasure, tpRatio, result_time, kcentroid)" +
                  "Values(?,?,?,?,?,?,?,?,?,?,?,?)");
          break;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void openConnection(String db_url, String user, String pw) throws SQLException {
    if (con == null) {
      con = DriverManager.getConnection(db_url, user, pw);
    }
  }


  public void writeEvaluationResult(long currentTime,
                                    double totalBudget, double iterationSize, String initialSelection,
                                    ModelType classifierType, double p, double r, double f, double alpha, double kCentroid) throws SQLException {
    stmt.setString(1, "N/A");
    stmt.setString(2, "N/A");
    stmt.setDouble(3, totalBudget);
    stmt.setDouble(4, iterationSize);
    stmt.setString(5, initialSelection);
    stmt.setString(6, getType(classifierType));
    stmt.setDouble(7, p);
    stmt.setDouble(8, r);
    stmt.setDouble(9, f);
    stmt.setDouble(10, alpha);
    stmt.setTimestamp(11, new Timestamp(currentTime));
    stmt.setDouble(12, kCentroid);
    stmt.executeUpdate();
  }



  private String getType(ModelType clusterType) {
    if (clusterType == ModelType.TREE) {
      return "tree";
    }else if (clusterType == ModelType.SVM) {
      return "svm";
    }else if (clusterType == ModelType.REGRESSION){
      return "regression";
    } else {
      return "NA";
    }
  }

  public static DatabaseResultWriter getInstance(DataSource source){
    if (self == null) {
      self = new DatabaseResultWriter(source);
    }
    return self;
  }

}
