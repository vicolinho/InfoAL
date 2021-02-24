package de.uni_leipzig.dbs.entity_resolution.io.reader;

import de.uni_leipzig.dbs.entity_resolution.io.IDDictionary;
import de.uni_leipzig.dbs.formRepository.dataModel.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by christen on 02.05.2017.
 */
public class CSVReader implements Reader {


  static int id =0;

  static int propertyID = 0;
  private  String entityId;
  private String srcID;
  private  String type;
  private String[] header;
  private static Map<String, GenericProperty> gps;


  public CSVReader(String entityId, String type, String srcId, String[] header){
    this.entityId = entityId;
    this.type = type;
    this.srcID = srcId;
    this.header = header;
    if (gps == null)
      gps = new HashMap<>();
  }
  @Override
  public Map<Integer, EntityStructureVersion> read(InputStream is) throws IOException {
    IDDictionary.getInstance().reset();
    Map<Integer,EntityStructureVersion> map = new HashMap<>();
    Map<Integer, Set<GenericEntity>>entityMap = new HashMap<>();

    CSVParser parser = new CSVParser(new InputStreamReader(is),CSVFormat.EXCEL
            .withDelimiter(',')
            .withHeader(header)
            .withQuote('"')
            .withEscape('\\')
            .withSkipHeaderRecord(true));
    Map<String, Integer> header = parser.getHeaderMap();

    int offSet = gps.size();
    for (Map.Entry<String, Integer> e: header.entrySet()){
      GenericProperty gp = new GenericProperty(offSet+e.getValue(), e.getKey(), null, null);
      if (!gps.containsKey(e.getKey())) {
        gps.put(gp.getName(), gp);
      }
    }
    List<CSVRecord> recordList =null;
    try {
      recordList = parser.getRecords();
    }catch (IOException e){e.printStackTrace();}
//    Iterator<CSVRecord> recordIterator = parser.iterator();
    for ( CSVRecord record :recordList){
      int srcIdentifier;
      if (srcID != null) {
        srcIdentifier = Integer.parseInt(record.get(srcID));
      }else {
        srcIdentifier =1;
      }
      String id = record.get(this.entityId);
      int recId;
      try {
        recId = Integer.parseInt(id);
    }catch (NumberFormatException nfe){
        recId = IDDictionary.getInstance().addId(id);
    }
      GenericEntity ge = new GenericEntity(recId,
              record.get(entityId), type, srcIdentifier);
      for (Map.Entry<String, Integer> e : header.entrySet()) {
        if (!e.equals(entityId)) {
          if (!record.get(e.getKey()).equals("--")&&!record.get(e.getKey()).equals("N/A")) {
            PropertyValue pv = new PropertyValue(propertyID++, record.get(e.getKey()));
            ge.addPropertyValue(gps.get(e.getKey()), pv);
          }
        }
      }
      Set<GenericEntity> entities = entityMap.get(ge.getSrcVersionStructureId());
      if (entities == null) {
        entities = new HashSet<>();
        entityMap.put(ge.getSrcVersionStructureId(), entities);
      }
      entities.add(ge);
    }


    for (Map.Entry<Integer, Set<GenericEntity>> e:entityMap.entrySet()){
      VersionMetadata vm = new VersionMetadata(e.getKey(),null, null,"testDataset","test");
      EntityStructureVersion esv = new EntityStructureVersion(vm);
      map.put(esv.getStructureId(), esv);
      for (GenericEntity ge: e.getValue()){
        esv.addEntity(ge);
      }
      for (GenericProperty property : gps.values()) {
        esv.addAvailableProperty(property);
      }
    }
    return map;
  }
}
