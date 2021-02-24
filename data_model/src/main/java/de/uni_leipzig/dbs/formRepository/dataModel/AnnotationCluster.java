package de.uni_leipzig.dbs.formRepository.dataModel;

import java.util.HashSet;
import java.util.Set;

/**
 * represents a cluster that is based on entities which are annotated with the same target concept
 * An cluster holds the entities and a set of terms that semantically describe the cluster. 
 * @author christen
 *
 */
public class AnnotationCluster extends GenericEntity{

	static int id =0;
	
	public static final String REPRESENTANT_PROPS = "representant";
	
	public static final String ENT_TYPE ="cluster";
	
	private boolean hasRepresentative;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4621121716156365756L;
	public static final int PROPERTY_REPRESNTANT_ID = -2;
	
	private EntitySet<GenericEntity> elements; 
	
	public AnnotationCluster(GenericEntity ge) {
		
		super(ge.getId(),ge.getAccession(),ENT_TYPE,-1);
		this.hasRepresentative =false;
	}
	
	public AnnotationCluster (int id, String accession, int clusterStructureId){
		super (id,accession,ENT_TYPE,clusterStructureId);
	}

	
	public void addRepresentant(String representant,String lang){
		GenericProperty gp = new GenericProperty(PROPERTY_REPRESNTANT_ID,REPRESENTANT_PROPS,null, lang);
		PropertyValue pv = new PropertyValue(id++,representant);
		this.addPropertyValue(gp, pv);
		this.hasRepresentative =true;
		
	}
	
	
	public Set <String> getRepresentants (){
		Set<String> values = new HashSet<String>();
		values.addAll(this.getPropertyValues(REPRESENTANT_PROPS, null,null));
		return values;
	}


	public EntitySet<GenericEntity> getElements() {
		return elements;
	}


	public void setElements(EntitySet<GenericEntity> elements) {
		this.elements = elements;
	}

	public boolean isHasRepresentative() {
		return hasRepresentative;
	}

	public void setHasRepresentative(boolean hasRepresentative) {
		this.hasRepresentative = hasRepresentative;
	}



	
	
}
