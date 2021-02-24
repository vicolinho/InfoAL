package de.uni_leipzig.dbs.formRepository.dataModel.encoding;

import java.util.Map;

import de.uni_leipzig.dbs.formRepository.dataModel.GenericProperty;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

public class EncodedEntityStructure {


  Int2IntMap objIds;
  int structureId;
  /**
   * array of encoded tokens the property values  [entityPosition][property Position][value id][token ID]
   */
  int[][][][] propertyValueIds;
  int[][][][] trigramIds;
  double[][][] numericValues;
  Map<GenericProperty, Integer> propertyPosition;



  Map<GenericProperty ,Integer> numericPropertyPosition;
  private Map<Integer, String> typeMap;


  public EncodedEntityStructure(int entStructId) {
    this.structureId = entStructId;
  }


  public int getStructureId() {
    return structureId;
  }

  public void setStructureId(int structureId) {
    this.structureId = structureId;
  }

  public Int2IntMap getObjIds() {
    return objIds;
  }

  public void setObjIds(Int2IntMap objIds) {
    this.objIds = objIds;
  }

  public Map<GenericProperty, Integer> getPropertyPosition() {
    return propertyPosition;
  }

  public void setPropertyPosition(Map<GenericProperty, Integer> propertyPosition) {
    this.propertyPosition = propertyPosition;
  }

  /**
   * @return array encoding the property values  [entityPosition][property Position][value id][token ID]
   */

  public int[][][][] getPropertyValueIds() {
    return propertyValueIds;
  }

  public void setPropertyValueIds(int[][][][] propertyValueIds) {
    this.propertyValueIds = propertyValueIds;
  }


  public int[][] getPropertyValues(int entId, GenericProperty gp) {

    int pos = this.objIds.get(entId);
    try {
      int propPos = this.propertyPosition.get(gp);
      return this.propertyValueIds[pos][propPos];
    } catch (NullPointerException e) {
      return new int[][]{};
    }
  }

  public int[][] getTrigramValues(int entId, GenericProperty gp) {

    int pos = this.objIds.get(entId);
    try {
      int propPos = this.propertyPosition.get(gp);
      return this.trigramIds[pos][propPos];
    } catch (NullPointerException e) {
      return new int[][]{};
    }
  }

  public int[][][] getPropertyValues(int entId) {

    int pos = this.objIds.get(entId);
    try {

      return this.propertyValueIds[pos];
    } catch (NullPointerException e) {
      return new int[][][]{};
    }
  }

  /**
   * @return array encoding the property values  [entityPosition][property Position][value id][trigram ID]
   */
  public int[][][][] getTrigramIds() {
    return trigramIds;
  }

  public void setTrigramIds(int[][][][] trigramIds) {
    this.trigramIds = trigramIds;
  }

  public void setTypeMap(Map<Integer, String> typeMap) {
    this.typeMap = typeMap;

  }

  public Map<Integer, String> getTypeMap() {
    return typeMap;
  }

  public Map<GenericProperty, Integer> getNumericPropertyPosition() {
    return numericPropertyPosition;
  }

  public void setNumericPropertyPosition(Map<GenericProperty, Integer> numericPropertyPosition) {
    this.numericPropertyPosition = numericPropertyPosition;
  }

  public double[][][] getNumericValues() {
    return numericValues;
  }

  public void setNumericValues(double[][][] numericValues) {
    this.numericValues = numericValues;
  }
}
