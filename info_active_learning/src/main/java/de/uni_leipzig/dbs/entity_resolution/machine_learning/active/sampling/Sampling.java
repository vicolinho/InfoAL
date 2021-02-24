package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.sampling;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Random;

/**
 * Created by christen on 13.06.2018.
 */
public interface Sampling {

  public LongSet getSample(Random r, Long2ObjectMap<Instance> vectors, Instances instances, int sample);


}
