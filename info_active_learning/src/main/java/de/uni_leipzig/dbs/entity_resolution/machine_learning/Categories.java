package de.uni_leipzig.dbs.entity_resolution.machine_learning;

import weka.core.Attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by christen on 06.08.2019.
 */
public class Categories {
  public static final List<String> categories = new ArrayList<>(Arrays.asList("yes", "no"));

  public static final Attribute classAttribute = new Attribute("class",Arrays.asList("yes","no"));
}
