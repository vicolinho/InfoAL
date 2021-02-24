package de.uni_leipzig.dbs.formRepository.dataModel.export;

import java.io.Serializable;

/**
 * Created by christen on 15.05.2017.
 */
public class ToolProperties implements Serializable{


  private String annotator;
  private String subset;
  private String parameter;
  private String selection;



  public ToolProperties() {
    annotator = "unknown";
    subset = "full";
    parameter = "none";
    selection = "none";
  }

  public String getAnnotator() {
    return annotator;
  }

  public void setAnnotator(String annotator) {
    this.annotator = annotator;
  }

  public String getSubset() {
    return subset;
  }

  public void setSubset(String subset) {
    this.subset = subset;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public String getSelection() {
    return selection;
  }

  public void setSelection(String selection) {
    this.selection = selection;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ToolProperties that = (ToolProperties) o;

    if (annotator != null ? !annotator.equals(that.annotator) : that.annotator != null) return false;
    if (parameter != null ? !parameter.equals(that.parameter) : that.parameter != null) return false;
    return selection != null ? selection.equals(that.selection) : that.selection == null;

  }

  @Override
  public int hashCode() {
    int result = annotator != null ? annotator.hashCode() : 0;
    result = 31 * result + (parameter != null ? parameter.hashCode() : 0);
    result = 31 * result + (selection != null ? selection.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ToolProperties{" +
            "annotator='" + annotator + '\'' +
            ", subset='" + subset + '\'' +
            ", parameter='" + parameter + '\'' +
            ", selection='" + selection + '\'' +
            '}';
  }
}


