package de.uni_leipzig.dbs.entity_resolution.evaluation;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by christen on 07.08.2019.
 */
public class CSVResultWriter {

  public static void writeResult(String file, String initialSelection, int iterationSize, int totalBudget,
                                 double p, double r, double f, double alpha, boolean append)
          throws IOException {
    FileWriter fw = new FileWriter(file, append);
    fw.append(initialSelection+",");
    fw.append(iterationSize +",");
    fw.append(totalBudget + ",");
    fw.append(alpha +",");
    fw.append(p + ",");
    fw.append(r + ",");
    fw.append(f + System.getProperty("line.separator"));
    fw.close();
  }
}
