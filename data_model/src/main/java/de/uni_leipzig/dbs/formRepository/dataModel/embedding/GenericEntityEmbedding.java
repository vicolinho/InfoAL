package de.uni_leipzig.dbs.formRepository.dataModel.embedding;

import de.uni_leipzig.dbs.formRepository.dataModel.GenericProperty;
import de.uni_leipzig.dbs.formRepository.dataModel.PropertySet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by christen on 08.12.2017.
 */
public class GenericEntityEmbedding implements IEmbeddedEntity {

  private int entityId;
  private PropertySet properties;
  private Int2ObjectMap<double[]> embeddingMap;
  private double[] entityEmbedding;


  public GenericEntityEmbedding(int id) {
    this.entityId = id;
    this.embeddingMap = new Int2ObjectOpenHashMap<>();
    properties = new PropertySet();
  }


  @Override
  public List<double[]> getEmbeddingForProperties(Set<GenericProperty> properties) {
    List<double[]> list = new ArrayList<>();
    for (GenericProperty gp : properties) {
      if (embeddingMap.containsKey(gp.getId())) {
        list.add(embeddingMap.get(gp.getId()));
      }
    }
    return list;
  }

  @Override
  public double[] getEmbeddingForProperty(GenericProperty properties) {
    if (embeddingMap.containsKey(properties.getId()))
      return embeddingMap.get(properties.getId());
    return new double[0];
  }

  @Override
  public void addEmbeddingForProperty(GenericProperty property, double[] embedding) {
    if (!properties.contains(property)) {
      properties.addProperty(property);
    }
    embeddingMap.put(property.getId(), embedding);
  }

  @Override
  public double[] getEmbeddingForEntity() {
    return entityEmbedding;
  }

  @Override
  public void setEmbeddingForEntity(double[] embeddingForEntity) {
    entityEmbedding = embeddingForEntity;
  }

  @Override
  public int getEntityId() {
    return entityId;
  }

  @Override
  public void setEntityId(int id) {
    this.entityId = id;
  }
}
