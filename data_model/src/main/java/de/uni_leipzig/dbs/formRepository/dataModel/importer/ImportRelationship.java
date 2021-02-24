package de.uni_leipzig.dbs.formRepository.dataModel.importer;

public class ImportRelationship {

	
	private String src ;
	
	private String target ; 
	
	private String type;
	
	boolean isDirected;
	
	public ImportRelationship(String src, String target, String type, boolean isDirected){
		this.src = src;
		this.target = target;
		this.type = type;
		this.isDirected = isDirected;
	}
	
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	} 
	
	public boolean isDirected() {
		return isDirected;
	}

	public void setDirected(boolean isDirected) {
		this.isDirected = isDirected;
	}

	@Override
	public String toString() {
		return "ImportRelationship{" +
						"src='" + src + '\'' +
						", target='" + target + '\'' +
						", type='" + type + '\'' +
						'}';
	}
}
