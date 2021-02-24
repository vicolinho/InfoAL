package de.uni_leipzig.dbs.formRepository.dataModel.export;

import de.uni_leipzig.dbs.formRepository.dataModel.AnnotationMapping;

/**
 * Created by lani on 10.05.17.
 */
public class ExportAnnotationMapping {

    private AnnotationMapping annotationMapping;

    String name;
    String annotator;
    String subset;
    String parameter;
    String selection;

    private ToolProperties toolProperties;

    public ExportAnnotationMapping(){
      toolProperties = new ToolProperties();
    }
    public String getSubset() {
        return toolProperties.getSubset();
    }

    public void setSubset(String subset) {
        this.toolProperties.setSubset(subset);
    }

    public String getParameter() {
        return toolProperties.getParameter();
    }

    public void setParameter(String parameter) {
        this.toolProperties.setParameter(parameter);
    }

    public String getSelection() {
        return toolProperties.getSelection();
    }

    public void setSelection(String selection) {
        this.toolProperties.setSelection(selection);
    }

    public String getAnnotator() {
        return toolProperties.getAnnotator();
    }

    public void setAnnotator(String annotator) {this.toolProperties.setAnnotator(annotator);}

    public String getName() {
        return name;
    }

    public AnnotationMapping getAm() {
        return annotationMapping;
    }

    public void setAm(AnnotationMapping annotations) {
        this.annotationMapping = annotations;
    }


}
