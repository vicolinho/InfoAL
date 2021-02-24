package de.uni_leipzig.dbs.formRepository.dataModel.graph.data;

public abstract class Edge {

	
	@Override
	public String toString() {
		return "Edge [type=" + type + ", srcId=" + srcId + ", targetId="
				+ targetId + "]";
	}
	String type; 
	
	int srcId;
	
	int targetId;

	float weight;
	
	float total;
	
	float cooccurCount; 
	
	
	public Edge(int srcId, int targetId,String type){
		this.srcId = srcId;
		this.targetId = targetId;
		this.type = type;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSrcId() {
		return srcId;
	}

	public void setSrcId(int srcId) {
		this.srcId = srcId;
	}

	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public float getCooccurCount() {
		return cooccurCount;
	}
	public void setCooccurCount(float cooccurCount) {
		this.cooccurCount = cooccurCount;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + srcId;
		result = prime * result + targetId;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (srcId != other.srcId)
			return false;
		if (targetId != other.targetId)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
