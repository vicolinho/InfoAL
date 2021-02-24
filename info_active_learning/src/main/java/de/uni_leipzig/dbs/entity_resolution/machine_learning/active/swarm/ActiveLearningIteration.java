package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm;


import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.InstancesGeneration;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.Oracle;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.selection.FarthestFirstSelection;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.comparators.WeightedUncoveredComparator;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.MLModel;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.ModelType;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.Training;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods.DecisionTree;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods.RandomForestWrapper;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.train.methods.SVMTraining;
import it.unimi.dsi.fastutil.longs.*;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Created by christen on 07.06.2018.
 */
public class ActiveLearningIteration {

  /**
   * number of links to be drawn for
   */
  private int iterationSize;

  /**
   * mapping between pair id cantor(src_id, target_id) and weka instance
   */
  private Long2ObjectMap<Instance> simVectors;

  /**
   * selected true positive examples
   */
  private Swarm tpSwarm;

  /**
   * selected true negative examples
   */
  private Swarm tnSwarm;


  /**
   * generate an Instances object for building a weka classification model
   */
  private InstancesGeneration generation;


  /**
   * used classification model (decision tree, SVM, regression)
   */
  private ModelType modelType;

  /**
   * instance for generating a classification model
   */
  private Training training;

  /**
   * representation of simvectors as particles that can move through the space
   */
  private Long2ObjectMap<Particle> allParticles;

  /**
   * already classified instances
   */
  private Long2ObjectMap<Instance> trainingData;


  /**
   * weight for entropy and uncertainty in the informativeness formula
   */
  private double alpha = 0.5;

  /**
   * already selected particles
   */
  private Set<Particle> allDrawnParticles;

  private MLModel classificationModel;

  private LongSet askedIds;

  /**
   * total number of selected similarity vectors
   */
  private int totalNumberOfSelection;

  /**
   *
   */
  private int budget;

  /**
   * number of selected similarity Vectors
   */
  private int selectedNumberForIteration;

  private LongSet newTrainingData;


  private FarthestFirstSelection circleSelection = new FarthestFirstSelection();
  private Long2DoubleMap overlap;

  private double oldUncertainity = 0;

  /**
   * @param iterationSize
   * @param simVectors mapping of pair id and weka instance
   * @param type classification model
   * @param initialSample initial selected instances
   * @param alpha
   */
  public ActiveLearningIteration(final int iterationSize, Long2ObjectMap<Instance> simVectors,
                                 ModelType type, LongSet initialSample, double alpha) {
    this.alpha = alpha;
    askedIds = new LongOpenHashSet();
    this.iterationSize = iterationSize;
    this.simVectors = simVectors;
    this.modelType = type;
    tpSwarm = new Swarm();
    tpSwarm.type = ParticleType.TP;
    tnSwarm = new Swarm();
    tnSwarm.type = ParticleType.TN;
    this.allParticles = initializeParticles(simVectors);
    generation = new InstancesGeneration();
    training = getTraining(modelType);
    allDrawnParticles = new HashSet<>();
    trainingData = requestOracle(initialSample);
    newTrainingData = new LongOpenHashSet();
    for (long id : trainingData.keySet()) {
      allDrawnParticles.add(allParticles.get(id));
      newTrainingData.add(id);
    }
    totalNumberOfSelection = initialSample.size();
  }


  /**
   * The id is computed with the record ids using Cantor.code
   * @return similarity vector ids for the oracle
   * @throws Exception
   */
  public LongSet iteration() throws Exception {
    List<Particle> orderedAmbiguityList = computeInformativeness();
    LongSet selectedInstances = selectInstances(orderedAmbiguityList, allParticles, allDrawnParticles, iterationSize);

//    Long2ObjectMap<Instance> newTrainingData = requestOracle(selectedInstances);
//    this.newTrainingData = new LongOpenHashSet(newTrainingData.keySet());
//    for (long id : newTrainingData.keySet()) {
//      allDrawnParticles.add(allParticles.get(id));
//    }
//    trainingData.putAll(newTrainingData);
    return selectedInstances;
  }

  private LongSet selectInstances(List<Particle> orderedAmbiguityList,
                                  Long2ObjectMap<Particle> allParticles, Set<Particle> drawnParticles, int drawK) {
    LongSet selectedInstances = new LongOpenHashSet();
    Long2ObjectMap<List<Pair>> map = new Long2ObjectOpenHashMap<>();
    double uncertainity = 0;
    for (int i = 0; i < orderedAmbiguityList.size(); i++) {
      Particle p = orderedAmbiguityList.get(i);
      List<Pair> selection;
      if (tpSwarm.getParticleList().size() != 0 && tnSwarm.getParticleList().size() != 0) {
        selection = getClosestNotDrawnBasedOnInformatives(p, allParticles, drawnParticles);
        double overlapMeasure = 1 / (1 + overlap.get(p.getLinkId()));
        double pUncertainity = selection.size() / (double) (allParticles.size() - drawnParticles.size());
        double overallMeasure = 2 * overlapMeasure * pUncertainity / (overlapMeasure + pUncertainity);
        uncertainity += overallMeasure;
      } else {
        selection = getKClosestNotDrawn(p, allParticles, drawnParticles);
      }
      if (selection.size() > 0) {
        map.put(p.getLinkId(), selection);
      }
    }
    uncertainity /= orderedAmbiguityList.size();
    if (oldUncertainity != 0 && uncertainity != 0) {
      drawK = (int) Math.ceil(iterationSize * uncertainity / oldUncertainity);
    } else {
      drawK = this.iterationSize;
    }
    oldUncertainity = uncertainity;
    LongList pIds = new LongArrayList();
    for (Particle p : orderedAmbiguityList) {
      if (map.containsKey(p.getLinkId()))
        pIds.add(p.getLinkId());
    }
    int index = 0;
    List<Pair> candidateList = new ArrayList<>();
    double maxSim = 0;
    boolean isFirst = true;
    for (long id : pIds) {
      for (Pair pr : map.get(id)) {
        if (pr.getSim() > maxSim && isFirst) {
          maxSim = pr.getSim();
          index = candidateList.size();
        }
        candidateList.add(pr);
      }
      isFirst = false;
    }

    int drawNumber = ((totalNumberOfSelection + drawK) < budget) ? drawK : budget - totalNumberOfSelection;
    List<Pair> selected = circleSelection.farthestFirstWekaImpl(candidateList, drawNumber, index);
    for (Pair pair : selected) {
      selectedInstances.add(pair.getP().getLinkId());
    }

    selectedNumberForIteration = selectedInstances.size();
    totalNumberOfSelection += selectedNumberForIteration;
    map.clear();
    selectedNumberForIteration = selectedInstances.size();
    return selectedInstances;
  }

  double currentEntropy = 0;

  /**
   * Select link instances based on the informativeness of a link. The informativeness is a combined measure based on the
   * entropy and the uncertainty. The entropy is defined as the ratio
   * between the number of instances belonging to the same class and the instances that not belong to same class in the
   * neighborhood. The uncertainty represents the coverage of the search space. A link is more relevant if the search
   * space is not covered by the selected training data.
   * @return
   */
  private List<Particle> computeInformativeness() {
    List<Particle> orderedList = new ArrayList<>();
    double infoExpectation = 0;
    int kNeighborhood = (int) Math.ceil(allDrawnParticles.size());

    for (Particle p : allDrawnParticles) {
      List<Pair> kClosestParticles = getSimilaritiesToNeighbors(kNeighborhood, p, allDrawnParticles);
      if (kClosestParticles.size() < kNeighborhood) {
        kNeighborhood = kClosestParticles.size();
      }
      double same = 0;
      double notSame = 0;
      double allDistance = 0;
      for (Pair pk : kClosestParticles) {
        if (p.getType().equals(pk.getP().getType())) {
          same += pk.getSim();
        } else {
          notSame += pk.getSim();
        }
        allDistance += (pk.getSim());
      }
      p.setSameNeighbours(same);
      p.setNotSameNeighbors(notSame);
      p.setAllDistance(allDistance);
    }
    int entropy0 = 0;
    overlap = this.computeOverlap(allDrawnParticles);
    for (Particle p : allDrawnParticles) {
      if (!askedIds.contains(p.getLinkId())) {
        double entropy = -(p.getSameNeighbours() / ((double) kNeighborhood)
                * Math.log(p.getSameNeighbours() / ((double) kNeighborhood))
                + p.getNotSameNeighbours() / ((double) kNeighborhood)
                * Math.log(p.getNotSameNeighbours() / ((double) kNeighborhood)));
        entropy = (Double.isNaN(entropy)) ? 0 : entropy;
        if (entropy == 0) {
          entropy0++;
        }
        p.setEntropy(entropy);
        double overlapUncertainity = overlap.containsKey(p.getLinkId()) ? 1 / (1 + overlap.get(p.getLinkId())) : 1;
        p.setInfoMeasure(alpha * entropy + (1 - alpha) * overlapUncertainity);
        orderedList.add(p);
      }
    }
    Collections.sort(orderedList, new WeightedUncoveredComparator());
    int nonZeros = 0;
    this.currentEntropy = 0;
    for (int i = 0; i < orderedList.size(); i++) {
      if (orderedList.get(i).getInfoMeasure() != 0) {
        infoExpectation += orderedList.get(i).getInfoMeasure();
        currentEntropy += orderedList.get(i).getEntropy();
        nonZeros++;
      }
    }
    infoExpectation /= (double) nonZeros;
    currentEntropy /= (orderedList.size() - entropy0);
    List<Particle> topKOrderedList = new ArrayList<>();
    for (int i = 0; i < orderedList.size(); i++) {
      Particle p = orderedList.get(i);
      if (p.getInfoMeasure() < infoExpectation) {
        break;
      } else {
        askedIds.add(p.getLinkId());
        topKOrderedList.add(p);
      }
    }
    return topKOrderedList;
  }

  private List<Pair> getSimilaritiesToNeighbors(int k, Particle p, Collection<Particle> particles) {
    List<Pair> orderedList = new ArrayList<>();
    for (Particle p2 : particles) {
      if (!p.equals(p2)) {
        double sim = p.computeSim(p2.getVectorPosition());
        Pair pair = new Pair(p2, sim);
        orderedList.add(pair);
      }
    }
    Collections.sort(orderedList);
    if (k < orderedList.size()) {
      return orderedList.subList(0, k);
    } else {
      return orderedList;
    }
  }

  int totalOverlap;

  private Long2DoubleMap computeOverlap(Set<Particle> drawnParticles) {
    Long2DoubleMap overlap = new Long2DoubleOpenHashMap();
    totalOverlap = 0;
    for (Particle p : drawnParticles) {
      double[] point;
      if (p.getType() == ParticleType.TP) {
        if (tnSwarm.getParticleList().size() > 0) {
          point = p.computeMoveVectorToClosest(tnSwarm, 1);
        } else {
          point = p.computeMoveVectorToClosest(tpSwarm, 1);
        }
        point = p.add(p.getVectorPosition(), point);

      } else {
        if (tpSwarm.getParticleList().size() > 0) {
          point = p.computeMoveVectorToClosest(tpSwarm, 1);
        } else {
          point = p.computeMoveVectorToClosest(tnSwarm, 1);
        }
        point = p.add(p.getVectorPosition(), point);
      }
      double sim2Center = p.euclideanDistance(point);
      for (Particle otherP : drawnParticles) {
        if (!p.equals(otherP) && !Arrays.equals(point, otherP.getVectorPosition())) {
          double sim = otherP.euclideanDistance(p.getVectorPosition());
          if (sim2Center > sim) {
            totalOverlap++;
            if (overlap.containsKey(p.getLinkId())) {
              overlap.put(p.getLinkId(), overlap.get(p.getLinkId()) + 1d);
            } else {
              overlap.put(p.getLinkId(), 1d);
            }
          }
        }
      } // each other particle
    }// each drawn particle
    return overlap;
  }

  /**
   * selects for a vector and the kOrder-based centroid the closest particle in the middle
   *
   * @param p
   * @param particles
   * @param drawnParticles
   * @return
   */
  private List<Pair> getClosestNotDrawnBasedOnInformatives(Particle p, Long2ObjectMap<Particle> particles,
                                                           Set<Particle> drawnParticles) {
    List<Pair> orderedList = new ArrayList<>();
    double[] point;
    if (p.getType() == ParticleType.TP) {
      if (this.tnSwarm.getParticleList().size() > 0) {
        point = p.computeMoveVectorToClosest(this.tnSwarm, 1);
      } else {
        point = p.computeMoveVectorToClosest(this.tpSwarm, 1);
      }
      point = p.add(p.getVectorPosition(), point);

    } else {
      if (tpSwarm.getParticleList().size() > 0) {
        point = p.computeMoveVectorToClosest(this.tpSwarm, 1);
      } else {
        point = p.computeMoveVectorToClosest(this.tnSwarm, 1);
      }
      point = p.add(p.getVectorPosition(), point);
    }
    double sim2Center = p.euclideanDistance(point);

    for (Map.Entry<Long, Particle> p2 : particles.long2ObjectEntrySet()) {
      if (p.getLinkId() != p2.getValue().getLinkId() && !drawnParticles.contains(p2.getValue())) {
        double sim = p2.getValue().euclideanDistance(p.getVectorPosition());
        if (sim2Center > sim) {
          Pair pair = new Pair(p2.getValue(), sim);
          orderedList.add(pair);
        }
      }
    }
    Collections.sort(orderedList);
    return orderedList;
  }

  private List<Pair> getKClosestNotDrawn(final Particle p, final Long2ObjectMap<Particle> particles,
                                         final Set<Particle> drawnParticles) {
    List<Pair> orderedList = new ArrayList<>();
    for (Map.Entry<Long, Particle> p2 : particles.long2ObjectEntrySet()) {
      if (p.getLinkId() != p2.getValue().getLinkId() && !drawnParticles.contains(p2.getValue())) {
        double sim = p.euclideanDistance(p2.getValue().getVectorPosition());
        Pair pair = new Pair(p2.getValue(), sim);
        orderedList.add(pair);
      }
    }
    Collections.sort(orderedList);
    return orderedList.subList(0, Math.round(iterationSize * 6));
  }


  private MLModel generateModel(Long2ObjectMap<Instance> allSamples) throws Exception {
    Instances dataset = generation.generateDataset(allSamples);
    MLModel model = training.train(dataset, SVMTraining.RBF);
    return model;
  }

  private Long2ObjectMap<Particle> initializeParticles(Long2ObjectMap<Instance> simVectors) {
    Long2ObjectMap<Particle> particles = new Long2ObjectOpenHashMap<>();
    for (Long2ObjectMap.Entry<Instance> e : simVectors.long2ObjectEntrySet()) {
      Particle p = new Particle(ParticleType.FN, e.getLongKey());
      double[] vec = new double[e.getValue().numAttributes() - 1];
      for (int i = 0; i < e.getValue().numAttributes() - 1; i++) {
        vec[i] = e.getValue().value(i);
      }
      p.setVectorPosition(vec);
      p.setMoveVector(new double[e.getValue().numAttributes() - 1]);
      particles.put(e.getLongKey(), p);
    }
    return particles;
  }

  public MLModel getClassificationModel() throws Exception {
    if (classificationModel == null) {
      classificationModel = generateModel(trainingData);
    }
    return classificationModel;
  }

  public Training getTraining(ModelType type) {
    Training training;
    switch (type) {
      case SVM:
        training = new SVMTraining();
        break;
      case TREE:
        training = new DecisionTree();
        break;
      case RANDOM_FOREST:
        training = new RandomForestWrapper();
        break;
      default:
        training = new DecisionTree();
    }
    return training;
  }

  public void updateTrainingData(Long2IntMap labeledVectors) {
    Long2ObjectMap<Instance> labeledInstances = new Long2ObjectOpenHashMap<>();
    for (Long2IntMap.Entry e : labeledVectors.long2IntEntrySet()) {
      Instance i = simVectors.get(e.getLongKey());
      labeledInstances.put(e.getLongKey(), i);
      if (e.getIntValue() == 1) {
        i.setValue(i.numAttributes() - 1, "yes");
        allParticles.get(e.getLongKey()).setType(ParticleType.TP);
        tpSwarm.addParticle(allParticles.get(e.getLongKey()));
      } else {
        i.setValue(i.numAttributes() - 1, "no");
        allParticles.get(e.getLongKey()).setType(ParticleType.TN);
        tnSwarm.addParticle(allParticles.get(e.getLongKey()));
      }
    }
    this.newTrainingData = new LongOpenHashSet(labeledInstances.keySet());
    for (long id : labeledInstances.keySet()) {
      allDrawnParticles.add(allParticles.get(id));
    }
    trainingData.putAll(labeledInstances);
  }

  private Long2ObjectMap<Instance> requestOracle(final LongSet sample) {
    Long2ObjectMap<Instance> labeledInstances = new Long2ObjectOpenHashMap<>();
    for (long linkId : sample) {
      Instance i = simVectors.get(linkId);
      labeledInstances.put(linkId, i);
      if (Oracle.getInstance().isMatch(linkId)) {
        i.setValue(i.numAttributes() - 1, "yes");
        allParticles.get(linkId).setType(ParticleType.TP);
        tpSwarm.addParticle(allParticles.get(linkId));
      } else {
        i.setValue(i.numAttributes() - 1, "no");
        allParticles.get(linkId).setType(ParticleType.TN);
        tnSwarm.addParticle(allParticles.get(linkId));
      }
    }
    return labeledInstances;
  }

  public void setBudget(int budget) {
    this.budget = budget;
  }


  /**
   * @return the number of drawn matches
   */
  public int getSelectedNumberForIteration() {
    return selectedNumberForIteration;
  }

}
