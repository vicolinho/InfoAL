package de.uni_leipzig.dbs.formRepository.dataModel.text;

import java.util.Arrays;
import java.util.List;

/**
 * Created by christen on 09.08.2018.
 */
public class Concept {

  private String ontology;

  private String accession;

  private List<String> values;

  private double[] embedding;

  public Concept(String ontology, String accession, List<String> values) {
    this.ontology = ontology;
    this.accession = accession;
    this.values = values;
  }

  public String getOntology() {
    return ontology;
  }

  public void setOntology(String ontology) {
    this.ontology = ontology;
  }

  public String getAccession() {
    return accession;
  }

  public void setAccession(String accession) {
    this.accession = accession;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public double[] getEmbedding() {
    return embedding;
  }

  public void setEmbedding(double[] embedding) {
    this.embedding = embedding;
  }

  public String toCSV(String delimiter) {
    StringBuilder sb = new StringBuilder();
    for (String value : values) {
      sb.append(accession + delimiter
              + value
              + ((embedding != null) ? (delimiter + Arrays.toString(embedding)) : delimiter+"")
              + System.getProperty("line.separator"));
    }
    return sb.toString();
  }
}
