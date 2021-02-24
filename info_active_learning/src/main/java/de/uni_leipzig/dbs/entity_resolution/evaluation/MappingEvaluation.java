package de.uni_leipzig.dbs.entity_resolution.evaluation;

import de.uni_leipzig.dbs.formRepository.dataModel.AnnotationMapping;
import de.uni_leipzig.dbs.formRepository.dataModel.EntityAnnotation;
import de.uni_leipzig.dbs.formRepository.dataModel.VersionMetadata;
import de.uni_leipzig.dbs.entity_resolution.util.SetAnnotationOperator;
import de.uni_leipzig.dbs.entity_resolution.util.AggregationFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MappingEvaluation {


  public EvaluationResult getResult (AnnotationMapping result, AnnotationMapping referenceMapping,
                                     String src, String target){
    EvaluationResult er = new EvaluationResult(src, target);
    AnnotationMapping correct = SetAnnotationOperator.intersect(AggregationFunction.MIN, result, referenceMapping);
    AnnotationMapping falsePositives = SetAnnotationOperator.diff( result, referenceMapping);
    AnnotationMapping falseNegatives = SetAnnotationOperator.diff( referenceMapping, result);
    er.setFalseNegative(falseNegatives, correct.getNumberOfAnnotations());
    er.setFalsePositives(falsePositives, correct.getNumberOfAnnotations());
    er.setTruePositive(correct);
    er.calculateFmeasure();
    return er;
  }
}
