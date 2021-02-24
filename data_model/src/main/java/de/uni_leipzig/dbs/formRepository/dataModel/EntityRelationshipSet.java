package de.uni_leipzig.dbs.formRepository.dataModel;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.HashSet;
import java.util.Set;


/**
 * holds a set of relationships between entities by storing for a generic entity the source id of the association as key 
 * and the target ids as set of {@link RelationshipSet} object as value. The different sets represent the different types
 * of relationships between the entities.
 * @author christen
 *
 */
public class EntityRelationshipSet {

	
	private Int2ObjectMap<Set<RelationshipSet>> relationMap;
	
	
	public EntityRelationshipSet (){
		relationMap = new Int2ObjectOpenHashMap<Set<RelationshipSet>>();
	}
	
	public void addRelationship (GenericEntity src, GenericEntity target, String type){
		Set<RelationshipSet> set = relationMap.get(src.getId());
		if (set ==null){
			set = new HashSet<RelationshipSet>();
			relationMap.put(src.getId(), set);
		}
		boolean exists =false;
		for (RelationshipSet rs: set){
			if (type.equals(rs.getType())){
				exists =true;
				rs.addTargetEntity(src,target);
				break;
			}
		}
		if (!exists){
			RelationshipSet rs = new RelationshipSet(type);
			set.add(rs);
			rs.addTargetEntity(src,target);
		}
	}
	
	
	
	public void removeRelationship (GenericEntity src,GenericEntity target, String type){
		Set<RelationshipSet> set = relationMap.get(src.getId());
		RelationshipSet remSet =null;
		boolean isFound =false;
		
		for (RelationshipSet rs: set){
			if (type.equals(rs.getType())){
				remSet = rs;
				remSet.removeTargetEntity(src.getId(),target.getId(),type);
				isFound = true;
				break;
			}
		}
		if (isFound){
			set.remove(remSet);
		}
	}
	
	public RelationshipSet getTargetEntities (GenericEntity src, String type){
		Set<RelationshipSet> set = relationMap.get(src.getId());
		for (RelationshipSet rs: set){
			if (type.equals(rs.getType())){
				return rs;	
			}
		}
		return new RelationshipSet(type);
		
	}
	
	/**
	 * return all related entities to the source entity
	 * @param src
	 * @return
	 */
	public Set<RelationshipSet> getTargetEntities (GenericEntity src){
		Set <RelationshipSet> set = new HashSet<RelationshipSet>();
		Set<RelationshipSet> originset = relationMap.get(src.getId());
		for (RelationshipSet rs: originset){
			set.add(rs);
		}
		return set;
	}
	
	
	public Set<EntityRelationship> getRelationships (){
		Set<EntityRelationship> map = new HashSet<EntityRelationship>();
		for (Set<RelationshipSet> set: this.relationMap.values()){
			for (RelationshipSet rs:set){
				map.addAll(rs.getRelationships());
			}
		}
		return map;
		
	}

	public void clear() {
		this.relationMap.clear();
		
	}
}
