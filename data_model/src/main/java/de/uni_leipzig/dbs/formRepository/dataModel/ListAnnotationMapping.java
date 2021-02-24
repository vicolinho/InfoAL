package de.uni_leipzig.dbs.formRepository.dataModel;

import de.uni_leipzig.dbs.formRepository.dataModel.util.CantorDecoder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.*;

/**
 * Created by christen on 11.04.2018.
 */
public class ListAnnotationMapping {

  private VersionMetadata srcVersion;

  private VersionMetadata targetVersion;

  private String name;

  private String method;
  private Long2ObjectMap<List<EntityAnnotation>> annotations;

  //private HashMap<Integer,Set<Long>> srcCorrespondenceMap;

  //private  HashMap<Integer,Set<Long>> targetCorrespondenceMap;

  private Long2ObjectMap<Set<Integer>> evidenceMap;


  public ListAnnotationMapping(VersionMetadata src, VersionMetadata target) {
    this();
    setSrcVersion(src);
    setTargetVersion(target);
  }


  public ListAnnotationMapping() {
    this.annotations = new Long2ObjectOpenHashMap<>();
    //srcCorrespondenceMap = new HashMap<Integer,Set<Long>>();
    //targetCorrespondenceMap = new HashMap<Integer,Set<Long>>();
    this.setEvidenceMap(new Long2ObjectOpenHashMap<Set<Integer>>());
  }

  public ListAnnotationMapping(int expectedSize) {
    this.annotations = new Long2ObjectOpenHashMap<>(expectedSize);
    //srcCorrespondenceMap = new HashMap<Integer,Set<Long>>();
    //targetCorrespondenceMap = new HashMap<Integer,Set<Long>>();
    this.setEvidenceMap(new Long2ObjectOpenHashMap<Set<Integer>>(expectedSize));
  }


  public void addAnnotation(EntityAnnotation a) {
    List<EntityAnnotation> annos = this.annotations.get(a.getId());
    if (annos == null) {
      annos = new ArrayList<>();
      this.annotations.put(a.getId(), annos);
    }
    annos.add(a);
  }

  public void removeAnnotation(int srcId, int targetId, int startPos, int endPos) {
    long code = CantorDecoder.code(srcId, targetId);
    if (annotations.containsKey(code)) {
      List<EntityAnnotation> list = annotations.get(code);
      if (list.size() ==1) {
        this.annotations.remove(code);
      }else {
        Iterator<EntityAnnotation> iterator = list.iterator();
        while (iterator.hasNext()) {
          EntityAnnotation ea = iterator.next();
          if (ea.getStartPos() == startPos && ea.getEndPos() == endPos) {
            iterator.remove();
          }
        }
        if (annotations.get(code).size()==0) {
          annotations.remove(code);
        }
      }
    }

  }

  public void removeAnnotation(long code, int startPos, int endPos) {
    if (annotations.containsKey(code)) {
      List<EntityAnnotation> list = annotations.get(code);
      if (list.size() ==1) {
        this.annotations.remove(code);
      }else {
        Iterator<EntityAnnotation> iterator = list.iterator();
        while (iterator.hasNext()) {
          EntityAnnotation ea = iterator.next();
          if (ea.getStartPos() == startPos && ea.getEndPos() == endPos) {
            iterator.remove();
          }
        }
        if (annotations.get(code).size()==0) {
          annotations.remove(code);
        }
      }
    }
  }

  public EntityAnnotation getAnnotation(long id, int startPos, int endPos) {

    if (this.annotations.containsKey(id)) {
      List<EntityAnnotation> list = annotations.get(id);
      for (EntityAnnotation ea: list) {
        if (ea.getStartPos() == startPos && ea.getEndPos() == endPos) {
          return ea;
        }
      }
    }
    return null;
  }

  public Collection<EntityAnnotation> getAnnotations() {
    List<EntityAnnotation> list = new ArrayList<>();
    annotations.values().forEach(l -> list.addAll(l));
    return list;
  }

  public EntityAnnotation getAnnotation(int srcId, int targetId, int startPos, int endPos) {
    long c = CantorDecoder.code(srcId, targetId);
    if (this.annotations.containsKey(c)) {
      List<EntityAnnotation> list = annotations.get(c);
      for (EntityAnnotation ea: list) {
        if (ea.getStartPos() == startPos && ea.getEndPos() == endPos) {
          return ea;
        }
      }
    }
    return null;
  }

  public Set<Integer> getCorrespondingTargetIds(int srcId, int startPos, int endPos) {
    Set<Integer> set = new HashSet<Integer>();
    for (EntityAnnotation ea : this.getAnnotations()) {
      if (ea.getSrcId() == srcId && startPos == ea.getStartPos() && ea.getEndPos() == endPos)
        set.add(ea.getTargetId());
    }
    return set;
  }

  public boolean containsCorrespondingTargetIds(int srcId, int startPos, int endPos) {
    for (EntityAnnotation ea : this.getAnnotations()) {
      if (srcId == ea.getSrcId() && startPos == ea.getStartPos() && ea.getEndPos() == endPos) {
        return true;
      }
    }
    return false;
    //return this.srcCorrespondenceMap.containsKey(srcId);
  }

  public Set<Integer> getCorrespondingSrcIds(int targetId, int startPos, int endPos) {
    Set<Integer> set = new HashSet<Integer>();
    for (EntityAnnotation ea : this.getAnnotations()) {
      if (ea.getTargetId() == targetId && startPos == ea.getStartPos() && ea.getEndPos() == endPos)
        set.add(ea.getSrcId());
    }
    return set;
  }

  public boolean containsCorrespondingSrcIds(int targetId, int startPos, int endPos) {
    for (EntityAnnotation ea : this.getAnnotations()) {
      if (targetId == ea.getTargetId() && startPos == ea.getStartPos() && ea.getEndPos() == endPos) {
        return true;
      }
    }
    return false;
  }

  public boolean contains(EntityAnnotation am) {
    if (this.annotations.containsKey(am.getId())) {
      List<EntityAnnotation> list = annotations.get(am.getId());
      for (EntityAnnotation ea: list) {
        if (ea.getStartPos() == am.getStartPos() && ea.getEndPos() == am.getEndPos()) {
          return true;
        }
      }
    }
    return false;

  }

  public boolean contains(long id, int startPos, int endPos) {
    if (this.annotations.containsKey(id)){
      List<EntityAnnotation> list = annotations.get(id);
      for (EntityAnnotation ea: list) {
        if (ea.getStartPos() == startPos && ea.getEndPos() == endPos) {
          return true;
        }
      }
    }
    return false;
  }

  public int getNumberOfAnnotations() {
    int size = 0;
    for (List<EntityAnnotation> l : annotations.values()) {
      size += l.size();
    }
   return size;
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
