package de.uni_leipzig.dbs.formRepository.dataModel;

import de.uni_leipzig.dbs.formRepository.dataModel.util.CantorDecoder;
/**
 * This class holds the association between a source entity and a target entity. The association store the ids accession and
 * the similarity.
 * @author christen
 *
 */
public class EntityAnnotation implements Comparable<EntityAnnotation>{

  private long id;


  private float sim;

  private float[] simVector;

  private int srcId;

  private int targetId;

  private String srcAccession;

  private String targetAccession;

  private boolean isVerfified;

  private int startPos;

  private int endPos;

  public static final float NO_SIM = -1f;

  public EntityAnnotation (int srcId,int targetId,String srcAcc, String targetAcc, float sim, boolean isVerified){
    id = CantorDecoder.code(srcId, targetId);
    this.srcAccession = srcAcc;
    this.targetAccession = targetAcc;
    this.sim = sim;
    this.srcId = srcId;
    this.targetId = targetId;
    this.setVerfified(isVerified);
    startPos = -1;
    endPos = -1;
  }

  public EntityAnnotation (long id,String srcAcc, String targetAcc, float sim, boolean isVerified){
    this.id = id ;
    srcId = (int) CantorDecoder.decode_a(id);
    targetId = (int) CantorDecoder.decode_b(id);
    this.srcAccession = srcAcc;
    this.targetAccession = targetAcc;
    this.sim = sim;
    this.setVerfified(isVerified);
  }

  @Override
  public String toString() {
    return "EntityAnnotation{" +
            "srcId=" + srcId +
            ", targetId=" + targetId +
            ", srcAccession='" + srcAccession + '\'' +
            ", targetAccession='" + targetAccession + '\'' +
            ", sim=" + sim +
            '}';
  }

  public int getSrcId(){
    return this.srcId;
  }

  public int getTargetId (){
    return this.targetId;
  }

  public float getSim() {
    return sim;
  }

  public void setSim(float sim) {
    this.sim = sim;
  }


  public String getTargetAccession() {
    return targetAccession;
  }

  public void setTargetAccession(String targetAccession) {
    this.targetAccession = targetAccession;
  }

  public String getSrcAccession() {
    return srcAccession;
  }

  public void setSrcAccession(String srcAccession) {
    this.srcAccession = srcAccession;
  }


  public long getId() {
    return id;
  }


  public void setId(long id) {
    this.id = id;
  }


  public boolean isVerfified() {
    return isVerfified;
  }


  public void setVerfified(boolean isVerfified) {
    this.isVerfified = isVerfified;
  }


  public void setSrcId(int srcId) {
    this.srcId = srcId;
  }


  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }

  public int compareTo(EntityAnnotation o) {
    Float i1 = this.sim;
    Float i2 = o.getSim();
    return i1.compareTo(i2);
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
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
    EntityAnnotation other = (EntityAnnotation) obj;
    return id == other.id;
  }

  public int getStartPos() {
    return startPos;
  }

  public void setStartPos(int startPos) {
    this.startPos = startPos;
  }

  public int getEndPos() {
    return endPos;
  }

  public void setEndPos(int endPos) {
    this.endPos = endPos;
  }

  public float[] getSimVector() {
    return simVector;
  }

  public void setSimVector(float[] simVector) {
    this.simVector = simVector;
  }
}
