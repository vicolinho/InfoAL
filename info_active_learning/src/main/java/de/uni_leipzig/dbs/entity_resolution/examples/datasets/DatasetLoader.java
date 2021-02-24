package de.uni_leipzig.dbs.entity_resolution.examples.datasets;

import de.uni_leipzig.dbs.entity_resolution.constants.*;
import de.uni_leipzig.dbs.entity_resolution.io.IDDictionary;
import de.uni_leipzig.dbs.entity_resolution.io.reader.CSVReader;
import de.uni_leipzig.dbs.formRepository.dataModel.*;
import de.uni_leipzig.dbs.formRepository.dataModel.util.CantorDecoder;

import java.io.*;
import java.util.*;

/**
 * Created by christen on 25.09.2017.
 */
public class DatasetLoader {




  public static final String MAPPING_AMAZON_PRODUCT = "E:/data/weightvectors/amazon-googleproducts-weight-vectors.csv/" +
          "amazon-googleproducts-weight-vectors_mod.csv";

  public static final String MAPPING_BIB_2 = "E:/data/weightvectors/dblp-gs-weight-vectors.csv/" +
          "dblp-gs-weight_reference.csv";

  public static final String MAPPING_MUSIC_PETER = "E:/data/weightvectors/musicbrainz-weight-vectors.csv/musicbrainz-weight-vectors.csv";

  public static final String MAPPING_CORA = "E:/data/weightvectors/cora-weight-vectors.csv/cora-weight-vectors.csv";

  public static final String MAPPING_NCVOTER = "E:/data/weightvectors/ncvr-weight-vectors.csv/ncvr-weight-vectors_mod.csv";




  /**
   * read a gold standard mapping for a certain data source from csv file following the format:<br>
   *
   * source_id, target_id, verified, sim_1, sim_2,..., sim_n <br>
   * ------------------------------------------------------<br>
   * source_id1, target_id1, 0, sim1, sim2,..., simn       <br>
   * ------------------------------------------------------<br>
   * source_id1, target_id2, 1, sim1, sim2,..., simn<br>
   *
   * @param dataSourcePath path of similarity vector file
   * @return
   * @throws IOException
   */
  //TODO should be generalized for arbitrary sources.
  public Map<Long, AnnotationMapping> getReferenceMappingForEvaluation(String dataSourcePath) throws IOException {
    Map<Long, AnnotationMapping> referenecMappings = new HashMap<>();
    AnnotationMapping referenceMapping = new AnnotationMapping();
    BufferedReader br = new BufferedReader(new FileReader(dataSourcePath));
    boolean isFirst = true;
    int tp =0;
    while (br.ready()) {
      String line = br.readLine();
      if (!isFirst) {
        String[] recPairs = line.split(",");
        boolean verified = (Double.parseDouble(recPairs[2]) == 1d);
        if (verified) {
          tp++;
          String srcRec = recPairs[0];
          String targetRec = recPairs[1];
          EntityAnnotation ea = new EntityAnnotation(Integer.parseInt(srcRec),
                  Integer.parseInt(targetRec), null, null, 1, true);
          referenceMapping.addAnnotation(ea);
        }
      }
      isFirst = false;
    }
    System.out.println(tp);
    referenecMappings.put(CantorDecoder.code(1,2), referenceMapping);
    return referenecMappings;
  }
}
