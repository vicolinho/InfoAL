package de.uni_leipzig.dbs.formRepository.dataModel.util;

import de.uni_leipzig.dbs.formRepository.dataModel.VersionMetadata;

/**
 * Created by christen on 17.05.2017.
 */
public class MappingNameGenerator {

  public static String getMappingName(VersionMetadata src, VersionMetadata target, String suffix){
    return  src.getName() + "[" + src.getTopic() + "]-"
            + target.getName() + "[" + target.getTopic() + "]"+((suffix!=null)?("_"+suffix):"");
  }
}
