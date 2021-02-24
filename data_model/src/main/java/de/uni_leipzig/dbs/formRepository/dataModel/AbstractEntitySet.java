package de.uni_leipzig.dbs.formRepository.dataModel;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * general implementation to organize object which implements IEntity
 * @author christen
 *
 * @param <T>
 */
public class AbstractEntitySet <T extends IEntity> implements EntitySet<T>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1114059484020045441L;



	public AbstractEntitySet (){
		this.objectMap = new Int2ObjectOpenHashMap<T>();
	}
	
	private Map <Integer,T> objectMap;

	
	
	public Collection<T> getCollection() {
		// TODO Auto-generated method stub
		return objectMap.values();
	}

	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return objectMap.values().iterator();
	}

	public void addEntity(T elem) {
		this.objectMap.put(elem.getId(), elem);	
	}
	
	public T getEntity (int id){
		return this.objectMap.get(id);
	}

	public void removeEntity(T elem) {
		this.objectMap.remove(elem.getId());
		
	}
	public void removeEntity (int id){
		objectMap.remove(id);
	}

	public boolean contains(T elem) {
		return this.objectMap.containsKey(elem.getId());
	}


	public boolean contains(int src) {
		
		return this.objectMap.containsKey(src);
	}


	public int getSize() {
		
		return this.objectMap.size();
	}

	public void clear() {
		this.objectMap.clear();
		
	}

	public void removeEntity(Integer geId) {
		this.objectMap.remove(geId);
		
	}
	public String toString(){
		return objectMap.toString();
	}
}
