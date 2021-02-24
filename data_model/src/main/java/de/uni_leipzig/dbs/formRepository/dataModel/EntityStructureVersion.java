package de.uni_leipzig.dbs.formRepository.dataModel;


import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.*;

public class EntityStructureVersion {

  /**
   * set of entities
   */
  private EntitySet<GenericEntity> entitySet ;
  /**
   * set of relationships
   */
  private EntityRelationshipSet relationshipSet;

  /**
   * mapping between accession of the accession of a GenericEntity and the id
   */
  private HashMap<String,Integer> accessionIdMap ;

  /**
   *number of GenericEntity for a specific type. This number is necessary for the idf value calculation
   */
  private HashMap<String,Integer> typeCount ;

  /**
   * available properties of the GenericEntities in the EntityStructureVersion
   */
  private PropertySet availableProperties;

  /**
   * metadata that holds the id of the structure in the repository, name, version and type
   */
  private VersionMetadata metadata ;


  public EntityStructureVersion (VersionMetadata metadata){
    this.entitySet =  new GenericEntitySet();
    relationshipSet = new EntityRelationshipSet();
    this.accessionIdMap = new HashMap<>();
    this.availableProperties = new PropertySet();
    this.typeCount = new HashMap<>();
    this.metadata = metadata;
  }

  public VersionMetadata getMetadata() {
    return metadata;
  }
  public void setMetadata(VersionMetadata metadata) {
    this.metadata = metadata;
  }

  public boolean contains (GenericEntity entity){
    return this.entitySet.contains(entity);
  }

  public void addEntity (GenericEntity entity){
    if (!this.entitySet.contains(entity)){
      this.entitySet.addEntity(entity);
      this.accessionIdMap.put(entity.getAccession(), entity.getId());
      if (typeCount.get(entity.getType())==null){
        typeCount.put(entity.getType(), 1);
      }else{
        typeCount.put(entity.getType(), 1+typeCount.get(entity.getType()));
      }
    }
  }

  public void removeEntity(GenericEntity entity){
    if (this.entitySet.contains(entity)){
      this.entitySet.removeEntity(entity);
      this.accessionIdMap.remove(entity.getAccession());
      typeCount.put(entity.getType(), typeCount.get(entity.getType())-1);
    }
  }

  public void removeEntity (int id){
    GenericEntity e  = this.entitySet.getEntity(id);
    this.entitySet.removeEntity(e);
    this.accessionIdMap.remove(e.getAccession());
    typeCount.put(e.getType(), typeCount.get(e.getType())-1);
  }

  public GenericEntity getEntity (int id){
    return this.entitySet.getEntity(id);
  }

  public GenericEntity getEntity (String accession){
    Integer id = this.accessionIdMap.get(accession);
    if (id!=null)
      return this.getEntity(id);
    return null;
  }

  public Collection<GenericEntity> getEntities(){
    return this.entitySet.getCollection();
  }

  public void addRelationship (GenericEntity src, GenericEntity target, String type)  {
    if (src!=null &&target!=null&& type!=null)
      this.relationshipSet.addRelationship(src, target, type);
  }



  public EntitySet<GenericEntity> getTargetEntities(GenericEntity src, String type){
    EntitySet<GenericEntity> es = new GenericEntitySet();
    if (src!=null && type !=null){
      RelationshipSet rs = this.relationshipSet.getTargetEntities(src, type);
      if (!rs.isEmpty()){
        for (EntityRelationship id :rs){
          es.addEntity(this.entitySet.getEntity(id.getTargetId()));
        }
      }
    }
    return es;
  }

  public Set<EntityRelationship> getRelationships (){
    return this.relationshipSet.getRelationships();
  }

  public int getNumberOfEntities(){
    return this.entitySet.getSize();
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

  public int getStructureId (){
    return this.metadata.getId();
  }


  public HashMap<String, Integer> getTypeCount() {
    return typeCount;
  }

  public void setTypeCount(HashMap<String, Integer> typeCount) {
    this.typeCount = typeCount;
  }

  public void clear(){
    this.entitySet.clear();
    this.availableProperties.clear();
    this.accessionIdMap.clear();
    this.relationshipSet.clear();
    //this.metadata = null;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
            + ((metadata == null) ? 0 : metadata.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EntityStructureVersion other = (EntityStructureVersion) obj;
    if (metadata == null) {
      if (other.metadata != null)
        return false;
    } else if (!metadata.equals(other.metadata))
      return false;
    return true;
  }

  public void deduplicateProperties(Set<GenericProperty> propsTarget){
    Map<String, IntSet> pvs = new HashMap<>();

    for (GenericEntity ge: this.getEntities()){
      for (GenericProperty gp : propsTarget) {
        List<String> pv = ge.getPropertyValues(gp);
        for (String v: pv){
          IntSet ids = pvs.get(v);
          if (ids ==null){
            ids = new IntOpenHashSet();
            pvs.put(v,ids);
          }
          ids.add(ge.getId());
        }
      }
    }

    for (Map.Entry<String, IntSet>e :pvs.entrySet()){
      if (e.getValue().size()>1){
        for (int id :e.getValue()){
          for (GenericProperty gp : propsTarget) {
            GenericEntity ge = this.getEntity(id);
            List<PropertyValue> newList = ge.getValues(gp);
            Iterator<PropertyValue> iter = newList.iterator();
            while (iter.hasNext()){
              PropertyValue p =iter.next();
              if (p.getValue().equals(e.getKey())){
                iter.remove();
              }
            }
            ge.changePropertyValues(gp,newList);
          }
        }
      }

    }

  }

}
