package de.uni_leipzig.dbs.formRepository.dataModel.importer;

import java.util.ArrayList;
import java.util.List;

public class ImportEntity {

	private String accession;
	
	private String type; 
	
	private List<ImportProperty> propertyMap;
	
	public ImportEntity (String accession, String type){
		this.accession = accession ;
		this.type = type;
		propertyMap = new ArrayList<ImportProperty>();
	}

	
	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	
	public void addProperty(ImportProperty prop){
		
		propertyMap.add(prop);
	}
	public List<ImportProperty> getProperties(){
		return this.propertyMap;
	}
	
	public void addProperty (String name, String value,String dataType,String lang,String scope){
		ImportProperty prop = new ImportProperty(name,value);
		prop.setDataType(dataType);prop.setLan(lang);prop.setScope(scope);
		propertyMap.add(prop);
	}

	@Override
	public String toString() {
		return "ImportEntity{" +
						"accession='" + accession + '\'' +
						", type='" + type + '\'' +
						", propertyMap=" + propertyMap +
						'}';
	}
}
