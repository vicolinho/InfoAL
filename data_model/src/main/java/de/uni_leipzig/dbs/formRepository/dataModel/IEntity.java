package de.uni_leipzig.dbs.formRepository.dataModel;

import java.io.Serializable;
import java.util.List;

public interface IEntity extends Serializable{

	void setId(int id);
	int getId();
	void setAccession(String acc);
	String getAccession();
	String getType();
	void setType(String type);
	//public StringPropertyValueSet getValues(GenericProperty property);
	void addPropertyValue(GenericProperty property, PropertyValue pv);
	void addPropertyValues(GenericProperty property, List<PropertyValue> pvs);
	List<PropertyValue> getValues(GenericProperty... properties);
	void removeProperty(GenericProperty property);
	
}
