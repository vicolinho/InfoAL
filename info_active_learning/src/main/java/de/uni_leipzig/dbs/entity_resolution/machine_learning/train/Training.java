package de.uni_leipzig.dbs.entity_resolution.machine_learning.train;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.MLModel;
import weka.core.Instances;

/**
 * Created by christen on 07.06.2017.
 */
public interface Training {

  public MLModel train(Instances trainingInstances, String function) throws Exception;
}
