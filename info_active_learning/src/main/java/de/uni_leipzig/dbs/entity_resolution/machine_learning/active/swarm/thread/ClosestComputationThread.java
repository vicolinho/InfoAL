package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.thread;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Pair;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Particle;
import de.uni_leipzig.dbs.entity_resolution.util.CosineSimComputation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by christen on 05.07.2018.
 */
public class ClosestComputationThread extends Thread {


  private double[] point;

  private Particle p;

  private List<Map.Entry<Long, Particle>> partition;

  private Set<Particle> drawnParticles;

  private List<Pair> orderedList;

  public ClosestComputationThread(Particle p, double[] point, List<Map.Entry<Long, Particle>> partition,
                                  Set<Particle> drawnParticles) {
    this.p = p;
    this.point = point;
    this.partition = partition;
    this.drawnParticles = drawnParticles;
  }

  @Override
  public void run(){
    double sim2Center = CosineSimComputation.sim(point, p.getVectorPosition());
    orderedList = new ArrayList<>();
    for (Map.Entry<Long, Particle> p2: partition) {
      if (p.getLinkId() != p2.getValue().getLinkId() && !drawnParticles.contains(p2.getValue())) {
        double sim = p2.getValue().computeSim(point);
        if (sim2Center <= sim) {
          Pair pair = new Pair(p2.getValue(), sim);
          orderedList.add(pair);
        }
      }
    }
  }

  public List<Pair> getOrderedList() {
    return orderedList;
  }

  public void release(){
    partition.clear();
    orderedList.clear();
  }

  public void setOrderedList(List<Pair> orderedList) {
    this.orderedList = orderedList;
  }
}
