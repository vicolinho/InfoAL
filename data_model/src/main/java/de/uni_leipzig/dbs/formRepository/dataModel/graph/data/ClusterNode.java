package de.uni_leipzig.dbs.formRepository.dataModel.graph.data;

public class ClusterNode extends Node{

	
	private int clazz;
	
	public ClusterNode(int id2, String type2) {
		super(id2, type2);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode(){
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public int getClazz() {
		return clazz;
	}
	public void setClazz(int clazz) {
		this.clazz = clazz;
	}

}
