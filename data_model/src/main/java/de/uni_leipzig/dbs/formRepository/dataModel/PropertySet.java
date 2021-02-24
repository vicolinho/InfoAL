package de.uni_leipzig.dbs.formRepository.dataModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class PropertySet {

	private Map<String,Set<GenericProperty>> propertyMap;
	Logger log =Logger.getLogger(getClass());
	public PropertySet (){
		propertyMap = new HashMap<String,Set<GenericProperty>>();
	}
	
	public void addProperty(GenericProperty prop){
		Set <GenericProperty> set = propertyMap.get(prop.getName());
		if (set==null){
			set = new HashSet<GenericProperty>();
			this.propertyMap.put(prop.getName(), set);
		}
		set.add(prop);
	}
	
	public void removeProperty (GenericProperty prop){
		this.propertyMap.remove(prop.getName());
	}
	
	
	public Set<GenericProperty> getProperty (String name, String scope, String lang){
		Set<GenericProperty> set = this.propertyMap.get(name);
		Set<GenericProperty> resultProperty = new HashSet<GenericProperty>();
		if (set!=null){
			if (scope!=null&& lang!=null){
				for (GenericProperty prop:set){
					if (prop.getLanguage().equals(lang)&&prop.getScope().equals(scope)){
						resultProperty.add(prop);
					}
				}
			}else if (scope==null &&lang!=null){
				for (GenericProperty prop:set){
					if (prop.getLanguage().equals(lang)){
						resultProperty.add(prop);
					}
				}
			}else if (scope!=null &&lang==null){
				for (GenericProperty prop:set){
					if (prop.getScope().equals(scope)){
						resultProperty.add(prop);
					}
				}
			}else{
				resultProperty.addAll(set);
				return resultProperty;
			}
		}
		return resultProperty;
	}
	
	public boolean contains (String propName){
		return this.propertyMap.containsKey(propName);
	}
	
	public boolean contains (String propName,String lang, String scope){
		Set<GenericProperty> set = this.propertyMap.get(propName);
		if (set!=null){
			if (lang == null && scope==null)
				return true;
			for (GenericProperty gp: set){
				if (lang != null && scope!=null){
					if (gp.getLanguage().equals(lang)&&gp.getScope().equals(scope)){
						return true;
					}
				}else if (lang == null && scope!=null){
					if (gp.getScope().equals(scope)){
						return true;
					}
				}else if (lang != null && scope==null){
					if (gp.getLanguage().equals(lang)){
						return true;
					}
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	public boolean contains (GenericProperty prop){
		Set<GenericProperty> set = this.propertyMap.get(prop.getName());
		if (set!=null){
			return set.contains(prop);
		}else{
			return false;
		}
	}
	
	public Collection<GenericProperty> getCollection() {
		// TODO Auto-generated method stub
		Collection <GenericProperty> props = new HashSet<GenericProperty>();
		for (Set<GenericProperty> set: this.propertyMap.values()){
			props.addAll(set);
		}
		return props;
	}

	public void clear() {
		this.propertyMap.clear();
		
	}
}
