package de.uni_leipzig.dbs.formRepository.dataModel;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.*;

import de.uni_leipzig.dbs.formRepository.dataModel.util.CantorDecoder;

/**
 * encoded Annotation Mapping that holds {EntityAnnotation} objects which only store the ids of the entities 
 * @author christen
 *
 */
public class EncodedAnnotationMapping extends AnnotationMapping{

	private Map <Long, EntityAnnotation> annotations;

	//private List<EntityAnnotation> annotations;
	private Long2ObjectMap<Set<Integer>> evidenceMap;
	
	
	public Long2ObjectMap<Set<Integer>> getEvidenceMap() {
		return evidenceMap;
	}


	public void setEvidenceMap(Long2ObjectMap<Set<Integer>> evidenceMap) {
		this.evidenceMap = evidenceMap;
	}


	public EncodedAnnotationMapping (){
		this.annotations = new HashMap<Long,EntityAnnotation>();
		//this.annotations = new ArrayList<EntityAnnotation>();
		evidenceMap = new Long2ObjectOpenHashMap<Set<Integer>>();
	}
	

	public void addAnnotation (EntityAnnotation a){
		this.annotations.put(a.getId(), a);
		//this.annotations.add(a);
	}

	public void removeAnnotation (int srcId,int targetId){
		long code  = CantorDecoder.code(srcId, targetId);
		this.annotations.remove(code);
	}

/*
	public EntityAnnotation getAnnotation (long id){
		return this.annotations.get(id);
	}
*/

	public Collection<EntityAnnotation> getAnnotations(){
		return this.annotations.values();
	}




	public EntityAnnotation getAnnotation (int srcId, int targetId){
		long c = CantorDecoder.code(srcId, targetId);
		return this.annotations.get(c);
	}

	public EntityAnnotation getAnnotation (long aid){
		return this.annotations.get(aid);
	}
/*
	public boolean contains(EntityAnnotation am){
		return this.annotations.containsKey(am.getId());
	}*/
	
	
	
	public int getNumberOfAnnotations(){
		return this.annotations.size();
	}
}
