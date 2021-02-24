package de.uni_leipzig.dbs.formRepository.dataModel.importer;

import de.uni_leipzig.dbs.formRepository.dataModel.VersionMetadata;

import java.util.HashMap;
import java.util.List;


/**
 * This class holds a set of annotations. Moreover, it consists of metadata like the annotation method, the name, and
 * the metadata of the entitystructures
 * @author christen
 *
 */
public class ImportAnnotationMapping {

	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private HashMap<String,List<ImportAnnotation>> annotations;

	

	public HashMap<String, List<ImportAnnotation>> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(HashMap<String, List<ImportAnnotation>> annotations) {
		this.annotations = annotations;
	}

	public VersionMetadata getSrcStruct() {
		return srcStruct;
	}

	public void setSrcStruct(VersionMetadata srcStruct) {
		this.srcStruct = srcStruct;
	}

	public VersionMetadata getTargetStruct() {
		return targetStruct;
	}

	public void setTargetStruct(VersionMetadata targetStruct) {
		this.targetStruct = targetStruct;
	}

	String method;
	
	String name;
	
	private VersionMetadata srcStruct;
	
	private VersionMetadata targetStruct;
}
