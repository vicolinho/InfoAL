package de.uni_leipzig.dbs.formRepository.dataModel.importer;

public class ImportProperty {

	
	private String value;
	
	private String scope;
	
	private String name;
	
	private String lan;
	
	private String dataType; 
	
	public ImportProperty(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}


	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
