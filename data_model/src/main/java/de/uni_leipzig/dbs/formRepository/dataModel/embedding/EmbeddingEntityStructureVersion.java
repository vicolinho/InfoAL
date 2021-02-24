package de.uni_leipzig.dbs.formRepository.dataModel.embedding;

import de.uni_leipzig.dbs.formRepository.dataModel.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.Collection;
import java.util.Set;

/**
 * Created by christen on 08.12.2017.
 */
public class EmbeddingEntityStructureVersion {

  /**
   * set of entity embedding
   */
  private Int2ObjectMap<GenericEntityEmbedding> entitySet;

  /**
   * available properties of the GenericEntities in the EntityStructureVersion
   */
  private PropertySet availableProperties;

  /**
   * metadata that holds the id of the structure in the repository, name, version and type
   */
  private VersionMetadata metadata ;

  public EmbeddingEntityStructureVersion (VersionMetadata metadata){
    this.entitySet =  new Int2ObjectLinkedOpenHashMap<>();
    this.availableProperties = new PropertySet();
    this.metadata = metadata;
  }

  public void addEmbedding(GenericEntityEmbedding gee) {
    this.entitySet.put(gee.getEntityId(), gee);
  }

  public Collection<GenericEntityEmbedding> getEmbeddings() {
    return entitySet.values();
  }

  public GenericEntityEmbedding getEmbedding(int id) {
    return this.entitySet.get(id);
  }

  public void addAvailableProperty(GenericProperty p){
    this.availableProperties.addProperty(p);
  }

  public Collection<GenericProperty> getAvailableProperties(){
    return this.availableProperties.getCollection();
  }

  public Set<GenericProperty> getAvailableProperties(String name, String lang, String scope){
    return this.availableProperties.getProperty(name, scope, lang);
  }

  public VersionMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(VersionMetadata metadata) {
    this.metadata = metadata;
  }
}
