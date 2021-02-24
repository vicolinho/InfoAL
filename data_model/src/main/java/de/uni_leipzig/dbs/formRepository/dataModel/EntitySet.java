package de.uni_leipzig.dbs.formRepository.dataModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;



public interface EntitySet <T extends IEntity> extends Serializable, Iterable<T>{

	
	
	public Collection<T> getCollection();
	
	public Iterator<T> iterator();
	
	public void addEntity(T elem);
	
	public void removeEntity(T elem);
	
	public boolean contains(T elem);
	
	public T getEntity(int id);

	public boolean contains(int src);
	
	public int getSize();
	
	public void clear();

	public void removeEntity(Integer geId);
}
