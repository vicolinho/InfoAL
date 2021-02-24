package de.uni_leipzig.dbs.formRepository.dataModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RelationshipSet implements Iterable<EntityRelationship> {

	

	String type;
	Map<Integer,EntityRelationship> targetIds ;
	
	public RelationshipSet (String type){
		this.type = type;
		this.targetIds = new HashMap<Integer,EntityRelationship>();
	}
	
	public void addTargetEntity (IEntity target,IEntity entity){
		EntityRelationship er = new EntityRelationship(target.getId(),
				entity.getId(), target.getAccession(),entity.getAccession(),type);
		this.targetIds.put(er.hashCode(),er);
	}
	
	public void removeTargetEntity (EntityRelationship er){
		this.targetIds.remove(er.hashCode());
	}
	
	public void removeTargetEntity (int src, int target,String type){
		
		this.targetIds.remove(this.hashCode(src, target, type));
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isEmpty(){
		return targetIds.isEmpty();
	}
	
	public Iterator<EntityRelationship> iterator(){
		return targetIds.values().iterator();
	}
	
	public Collection<EntityRelationship> getRelationships(){
		return this.targetIds.values();
	}
	
	
	
	public int hashCode(int srcId, int targetId, String type) {
		final int prime = 31;
		int result = 1;
		result = prime * result + srcId;
		result = prime * result + targetId;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		RelationshipSet other = (RelationshipSet) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
