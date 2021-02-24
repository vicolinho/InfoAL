package de.uni_leipzig.dbs.formRepository.dataModel.text;

import de.uni_leipzig.dbs.formRepository.dataModel.VersionMetadata;

import java.util.Map;
import java.util.Set;

/**
 * Created by christen on 04.05.2018.
 */
public class DocumentCandidates {

  private VersionMetadata docMeta;

  private Map<Mention, Set<String>> candidates;


  public VersionMetadata getDocMeta() {
    return docMeta;
  }

  public void setDocMeta(VersionMetadata docMeta) {
    this.docMeta = docMeta;
  }

  public Map<Mention, Set<String>> getCandidates() {
    return candidates;
  }

  public void setCandidates(Map<Mention, Set<String>> candidates) {
    this.candidates = candidates;
  }

}
