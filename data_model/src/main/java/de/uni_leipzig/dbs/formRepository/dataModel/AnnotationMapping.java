package de.uni_leipzig.dbs.formRepository.dataModel;

import de.uni_leipzig.dbs.formRepository.dataModel.util.CantorDecoder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.*;

/**
 * This class store an AnnotationMapping between a source {@link EntityStructureVersion} object and
 * a target {@link EntityStructureVersion} object
 *
 * @author christen
 */
public class AnnotationMapping {

  private VersionMetadata srcVersion;

  private VersionMetadata targetVersion;

  private String name;

  private String method;
  private Long2ObjectMap<EntityAnnotation> annotations;

  //private HashMap<Integer,Set<Long>> srcCorrespondenceMap;

  //private  HashMap<Integer,Set<Long>> targetCorrespondenceMap;

  private Long2ObjectMap<Set<Integer>> evidenceMap;


  public AnnotationMapping(VersionMetadata src, VersionMetadata target) {
    this();
    setSrcVersion(src);
    setTargetVersion(target);
  }


  public AnnotationMapping() {
    this.annotations = new Long2ObjectOpenHashMap<>();
    //srcCorrespondenceMap = new HashMap<Integer,Set<Long>>();
    //targetCorrespondenceMap = new HashMap<Integer,Set<Long>>();
    this.setEvidenceMap(new Long2ObjectOpenHashMap<Set<Integer>>());
  }

  public AnnotationMapping(int expectedSize) {
    this.annotations = new Long2ObjectOpenHashMap<>(expectedSize);
    //srcCorrespondenceMap = new HashMap<Integer,Set<Long>>();
    //targetCorrespondenceMap = new HashMap<Integer,Set<Long>>();
    this.setEvidenceMap(new Long2ObjectOpenHashMap<Set<Integer>>(expectedSize));
  }


  public void addAnnotation(EntityAnnotation a) {
    this.annotations.put(a.getId(), a);
  }

  public void removeAnnotation(int srcId, int targetId) {
    long code = CantorDecoder.code(srcId, targetId);
    this.annotations.remove(code);
  }

  public void removeAnnotation(long code) {
    this.annotations.remove(code);
  }

  public EntityAnnotation getAnnotation(long id) {
    return this.annotations.get(id);
  }

  public Collection<EntityAnnotation> getAnnotations() {
    return this.annotations.values();
  }

  public EntityAnnotation getAnnotation(int srcId, int targetId) {
    long c = CantorDecoder.code(srcId, targetId);
    return this.annotations.get(c);
  }

  public Set<Integer> getCorrespondingTargetIds(int srcId) {
    Set<Integer> set = new HashSet<Integer>();
    for (EntityAnnotation ea : this.getAnnotations()) {
      if (ea.getSrcId() == srcId)
        set.add(ea.getTargetId());
    }
    return set;
  }

  public boolean containsCorrespondingTargetIds(int srcId) {
    for (EntityAnnotation ea : this.getAnnotations()) {
      if (srcId == ea.getSrcId()) {
        return true;
      }
    }
    return false;
    //return this.srcCorrespondenceMap.containsKey(srcId);
  }

  public Set<Integer> getCorrespondingSrcIds(int targetId) {
    Set<Integer> set = new HashSet<Integer>();
    for (EntityAnnotation ea : this.getAnnotations()) {
      if (ea.getTargetId() == targetId)
        set.add(ea.getSrcId());
    }
    return set;
  }

  public boolean containsCorrespondingSrcIds(int targetId) {
    for (EntityAnnotation ea : this.getAnnotations()) {
      if (targetId == ea.getTargetId()) {
        return true;
      }
    }
    return false;
  }

  public boolean contains(EntityAnnotation am) {
    return this.annotations.containsKey(am.getId());
  }

  public boolean contains(long id) {
    return this.annotations.containsKey(id);
  }

  public int getNumberOfAnnotations() {
    return this.annotations.size();
  }


  public String getMethod() {
    return method;
  }


  public void setMethod(String method) {
    this.method = method;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public VersionMetadata getSrcVersion() {
    return srcVersion;
  }


  public void setSrcVersion(VersionMetadata srcVersion) {
    this.srcVersion = srcVersion;
  }


  public VersionMetadata getTargetVersion() {
    return targetVersion;
  }


  public void setTargetVersion(VersionMetadata targetVersion) {
    this.targetVersion = targetVersion;
  }

  public Long2ObjectMap<Set<Integer>> getEvidenceMap() {
    return evidenceMap;
  }


  public void setEvidenceMap(Long2ObjectMap<Set<Integer>> long2ObjectMap) {
    this.evidenceMap = long2ObjectMap;
  }


  public String toString() {
    return this.annotations.values().toString();
  }


}
