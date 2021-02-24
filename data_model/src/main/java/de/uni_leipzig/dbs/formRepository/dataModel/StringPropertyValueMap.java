package de.uni_leipzig.dbs.formRepository.dataModel;

import java.io.Serializable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class StringPropertyValueMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8915905103700741243L;
	private Int2ObjectMap<IntOpenHashSet> valueMap; 
	
	public StringPropertyValueMap (){
		valueMap = new Int2ObjectOpenHashMap<IntOpenHashSet>();
	}
	public void addPropertyValue (GenericProperty property, PropertyValue pv){
		IntOpenHashSet values = valueMap.get(property.getId());
		if (values ==null){
			values = new IntOpenHashSet();
			this.valueMap.put(property.getId(), values);
		}
		values.add(pv.getId());
	}
	
	public void addPropertyValue (GenericProperty property, StringPropertyValueSet pvs){
		IntOpenHashSet values = valueMap.get(property.getId());
		if (values ==null){
			values = new IntOpenHashSet();
			this.valueMap.put(property.getId(), values);
		}
		for (PropertyValue pv : pvs.getCollection()){
			values.add(pv.getId());
		}
		
	}
	
	public void removeProperty (GenericProperty property){
		this.valueMap.remove(property.getId());
	}
	
	public void removePropertyValue(GenericProperty property,PropertyValue pv){
		IntOpenHashSet set = this.valueMap.get(property.getId());
		if (set!=null){
			set.rem(pv.getId());
			if (set.size()==0){
				this.valueMap.remove(property.getId());
			}
		}
	}
	
	public IntOpenHashSet getPropertyValueIds (GenericProperty property){
		return this.valueMap.get(property.getId());
	}
	
	public IntOpenHashSet getPropertyValueIds (GenericProperty... properties){
		IntOpenHashSet set = new IntOpenHashSet ();
		for (GenericProperty prop: properties){
			IntOpenHashSet part = this.valueMap.get(prop.getId());
			if (part!=null){
				set.addAll(part);
			}
		}
		return set;
	}
	
}
