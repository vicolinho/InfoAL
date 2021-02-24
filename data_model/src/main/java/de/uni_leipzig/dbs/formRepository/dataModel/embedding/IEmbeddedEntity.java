package de.uni_leipzig.dbs.formRepository.dataModel.embedding;

import de.uni_leipzig.dbs.formRepository.dataModel.GenericProperty;

import java.util.List;
import java.util.Set;

/**
 * Created by christen on 08.12.2017.
 */
public interface IEmbeddedEntity {

  public List<double[]> getEmbeddingForProperties(Set<GenericProperty> properties);
  public double[] getEmbeddingForProperty(GenericProperty properties);
  public void addEmbeddingForProperty(GenericProperty property, double[] embedding);
  public double[] getEmbeddingForEntity();
  public void setEmbeddingForEntity(double [] embeddingForEntity);
  public int getEntityId();
  public void setEntityId(int id);
}
