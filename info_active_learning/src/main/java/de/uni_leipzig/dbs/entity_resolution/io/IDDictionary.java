package de.uni_leipzig.dbs.entity_resolution.io;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by christen on 17.09.2017.
 */
public class IDDictionary {

  static int numericId = 0;
  private Map<String, Integer> id2IntegerMap;
  private static IDDictionary self;
  private IDDictionary(){
    id2IntegerMap = new HashMap<>();
  }

  public int addId (String id) {
    if (id2IntegerMap.containsKey(id)) {
      return id2IntegerMap.get(id);
    }else {
      int newId = numericId;
      id2IntegerMap.put(id, newId);
      numericId++;
      return newId;
    }
  }

  public int getId (String key) {
    return id2IntegerMap.get(key);
  }


  public static IDDictionary getInstance () {
    if (self == null) {
      self = new IDDictionary();
    }
    return self;
  }

  public void reset() {
    numericId=0;
  }
}
