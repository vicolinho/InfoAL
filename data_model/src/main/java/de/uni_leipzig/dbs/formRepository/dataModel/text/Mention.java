package de.uni_leipzig.dbs.formRepository.dataModel.text;

import java.util.Arrays;

/**
 * Created by christen on 03.05.2018.
 */
public class Mention {

  private String value;

  private String ctx;

  private int start;

  private int end;

  private String accession;

  public double[] getEmbedding() {
    return embedding;
  }

  public void setEmbedding(double[] embedding) {
    this.embedding = embedding;
  }

  private double[] embedding;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public String getAccession() {
    return accession;
  }

  public void setAccession(String accession) {
    this.accession = accession;
  }


  public String getCtx() {
    return ctx;
  }

  public void setCtx(String ctx) {
    this.ctx = ctx;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Mention mention = (Mention) o;

    if (start != mention.start) return false;
    if (end != mention.end) return false;
    return accession.equals(mention.accession);

  }

  @Override
  public int hashCode() {
    int result = start;
    result = 31 * result + end;
    result = 31 * result + accession.hashCode();
    return result;
  }

  public String toCSV(String delimitier) {
    return accession + delimitier
            + hashCode() + delimitier
            + start + delimitier
            + end + delimitier
            + value + delimitier
            + ctx + delimitier
            + ((embedding != null) ? (Arrays.toString(embedding)) : "");
  }
}
