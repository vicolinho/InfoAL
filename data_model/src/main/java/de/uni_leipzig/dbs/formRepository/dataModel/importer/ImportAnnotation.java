package de.uni_leipzig.dbs.formRepository.dataModel.importer;

/**
 * bean class that represents an annotation. An annotation is an association between a real
 * world object and a concept of an ontology
 * @author christen
 *
 */
public class ImportAnnotation {

	/**
	 * real-world identifier
	 */
	
	private String srcAccession;

	/**
	 * concept identifier
	 */
	private String targetAccession;
	
	
	/**
	 * similarity of the annotation
	 */
	private float sim;
	
	private boolean isVerified;

	private int startPosition;

	private int endPosition;
	
	
	public ImportAnnotation(String srcAccession, String targetAccession,
			float sim, boolean isVerified) {
		super();
		this.srcAccession = srcAccession;
		this.targetAccession = targetAccession;
		this.sim = sim;
		this.isVerified = isVerified;
		startPosition =-1;
		endPosition = -1;
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


	public float getSim() {
		return sim;
	}


	public void setSim(float sim) {
		this.sim = sim;
	}


	public boolean isVerified() {
		return isVerified;
	}


	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	@Override
	public String toString() {
		return "ImportAnnotation{" +
						"srcAccession='" + srcAccession + '\'' +
						", targetAccession='" + targetAccession + '\'' +
						", startPosition=" + startPosition +
						", endPosition=" + endPosition +
						'}';
	}
}

