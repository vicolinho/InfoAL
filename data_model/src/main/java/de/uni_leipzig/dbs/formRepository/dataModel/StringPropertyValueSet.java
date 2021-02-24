package de.uni_leipzig.dbs.formRepository.dataModel;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class StringPropertyValueSet implements Serializable, Iterable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8383258653303351394L;
	private Int2ObjectMap<PropertyValue> map ;
	
	public StringPropertyValueSet (){
		this.map = new Int2ObjectOpenHashMap<PropertyValue>();
	}
	
	public void addPropertyValue(PropertyValue pv){
		if (map.get(pv.getId())==null){
			
			map.put(pv.getId(), pv);
		}
	}
	
	
	
	public void addPropertyValues (StringPropertyValueSet pvs){
		for (Entry<Integer, PropertyValue>   e: pvs.map.entrySet()){
			if (!map.containsKey(e.getKey())){
				map.put(e.getKey(), e.getValue());
			}
		}
	}
	
	public void changePropertyValues(int pvId, String newValue){
		PropertyValue pv = this.map.get(pvId);
		pv.setValue(newValue);
	}
	
	public void changePropertyValues(IntOpenHashSet pvIds,
			List<String> newValues) {
		int index =0;
		for (int i : pvIds){
			if (this.map.containsKey(i)){
				PropertyValue pv =  this.map.get(i);
				pv.setValue(newValues.get(index++));
			}
		}
	}
	
	public Collection <PropertyValue> getCollection(){
		return this.map.values();
	}
	
	public List<String> getValues (int pvId){
		List<String> values = new ArrayList<String>();
		if (this.map.containsKey(pvId)){
			values.add(this.map.get(pvId).getValue());
		}
		return values;
	}
	
	public List<String> getValues (IntOpenHashSet pvs){
		List<String> values = new ArrayList<String>();
		for (int i : pvs){
			if (this.map.containsKey(i)){
				values.add(this.map.get(i).getValue());
			}
		}
		return values;
	}
	
	public StringPropertyValueSet getPropertyValue(int pvId){
		StringPropertyValueSet values = new StringPropertyValueSet();
		if (this.map.containsKey(pvId)){
			PropertyValue pv = new PropertyValue (pvId,map.get(pvId).getValue());
			values.addPropertyValue(pv);
		}
		return values;
	}
	
	public StringPropertyValueSet getPropertyValues (IntOpenHashSet pvs){
		StringPropertyValueSet values = new StringPropertyValueSet();
		for (int i : pvs){
			if (this.map.containsKey(i)){
				PropertyValue pv = new PropertyValue (i,map.get(i).getValue());
				values.addPropertyValue(pv);
			}
		}
		return values;
	}
	
	public void removePropertyValues (int pvId){
		if (this.map.containsKey(pvId)){
			this.map.remove(pvId);
		}
	}
	
	public void removePropertyValues (IntOpenHashSet pvIds){
		for (int pvId: pvIds){
			if (this.map.containsKey(pvId)){
				this.map.remove(pvId);
			}
		}
	}
	
	

	public Iterator<PropertyValue> iterator() {
		return map.values().iterator();
	}

	public void changePropertyValues(StringPropertyValueSet newValues) {
		for (PropertyValue pv : newValues.getCollection()){
			this.map.put(pv.getId(), pv);
		}
		
	}

	

}
