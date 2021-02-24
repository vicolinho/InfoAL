package de.uni_leipzig.dbs.formRepository.dataModel;

public class EntityRelationship {

	
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
		EntityRelationship other = (EntityRelationship) obj;
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

	private String type;
	
	private int srcId;
	private int targetId;
	private String srcAccession;
	private String targetAccession;
	
	public EntityRelationship( int srcId, int targetId,
			String srcAccession,String targetAccession, String type) {
		super();
		this.type = type;
		this.srcId = srcId;
		this.targetId = targetId;
		this.srcAccession = srcAccession;
		this.targetAccession = targetAccession;
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

	public String getSrcAccession() {
		return srcAccession;
	}

	public void setSrcAccession(String srcAccession) {
		this.srcAccession = srcAccession;
	}

	public String getTargetAccession() {
		return targetAccession;
	}

	public void setTargetAccession(String targetAccession) {
		this.targetAccession = targetAccession;
	}
}
