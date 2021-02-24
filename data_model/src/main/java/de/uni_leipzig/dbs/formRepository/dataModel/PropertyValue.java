package de.uni_leipzig.dbs.formRepository.dataModel;

import java.io.Serializable;

public class PropertyValue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6223583083651646683L;
	private int id;
	private String value;
	
	public PropertyValue(){
		
	}
	
	public PropertyValue(int id, String value){
		this.id = id;
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PropertyValue [id=" + id + ", value=" + value + "]";
	}
}
