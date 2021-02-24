package de.uni_leipzig.dbs.entity_resolution.examples.machineLearning.active;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.selection.FarthestFirstSelection;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Pair;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Particle;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.ParticleType;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Swarm;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.comparators.WeightedUncoveredComparator;
import de.uni_leipzig.dbs.entity_resolution.util.CosineSimComputation;
import it.unimi.dsi.fastutil.longs.*;
import org.apache.log4j.Logger;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by christen on 19.09.2018.
 */
public class FarthestFirstExample {


  static Swarm tpSwarm = new Swarm();

  static Swarm tnSwarm = new Swarm();


  static Set<Particle> allDrawnParticles = new HashSet<>();

  static Long2ObjectMap<Particle> allParticles = new Long2ObjectOpenHashMap<>();

  public static void main(String[] args) throws IOException {
    InputStream is = ClassLoader.getSystemResourceAsStream("examplesInfo2.txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    long id = 0;
    while (br.ready()) {
      String line = br.readLine();
      double[] vec= new double[2];
      vec[0] = Double.parseDouble(line.split(";")[1]);
      vec[1] = Double.parseDouble(line.split(";")[2]);
      ParticleType pt;
      if (Integer.parseInt(line.split(";")[3])==1) {
        pt = ParticleType.TP;
      } else if (Integer.parseInt(line.split(";")[3])==0) {
        pt = ParticleType.TN;
      } else {
        pt = ParticleType.FN;
      }
      Particle p = new Particle(pt, Long.parseLong(line.split(";")[0]));
      p.setVectorPosition(vec);
      if (pt.equals(ParticleType.TP)) {
        tpSwarm.addParticle(p);
        allDrawnParticles.add(p);
      } else if (pt.equals(ParticleType.TN)){
        tnSwarm.addParticle(p);
        allDrawnParticles.add(p);
      }
      allParticles.put(p.getLinkId(), p);

    }
    br.close();
    getSample(new Random(), allParticles, 6);
  }

  public static LongSet getSample(Random r, Long2ObjectMap<Particle> vectors, int sample) {
    List<Map.Entry<Long, Particle>> instanceList = new ArrayList<>(vectors.long2ObjectEntrySet());
//    Collections.shuffle(instanceList, r);
//    int startIndex = r.nextInt((instanceList.size())+1)+0;
    LongSet sampleSet = new LongOpenHashSet();
    boolean[] selected = new boolean[instanceList.size()];
    double[] minDistance = new double[instanceList.size()];
    for(int i = 0; i < minDistance.length; i++) {
      minDistance[i] = Double.MAX_VALUE;
    }
    int index = 0;
    for (Map.Entry<Long,Particle> e: instanceList) {
      if (e.getKey() == 6) {
        break;
      } else {
        index++;
      }
    }
    Map.Entry<Long, Particle> startElement = instanceList.get(index);
    selected[index] = true;
    sampleSet.add(startElement.getKey().longValue());
    minDistance = updateMinDistance(minDistance, selected, instanceList, startElement.getValue());
    for(int i = 1; i < sample; i++) {
      int nextI =  farthestAway(minDistance, selected);
      sampleSet.add(instanceList.get(nextI).getKey());
      selected[nextI] = true;
      updateMinDistance(minDistance, selected, instanceList, instanceList.get(nextI).getValue());
    }
    return sampleSet;
  }

  private static double[] updateMinDistance(double[] minDistance, boolean[] selected,
                                       List<Map.Entry<Long,Particle>> data, Particle center) {
    for(int i = 0; i<selected.length; i++) {
      if (!selected[i]) {
        double d = CosineSimComputation.euclideanDistance(center.getVectorPosition(),
                data.get(i).getValue().getVectorPosition());
        if (d < minDistance[i])
          minDistance[i] = d;
      }
    }
    return minDistance;
  }

  private static int farthestAway(double[] minDistance, boolean[] selected) {
    double maxDistance = -1.0;
    int maxI = -1;
    for(int i = 0; i < selected.length; i++)
      if (!selected[i])
        if (maxDistance < minDistance[i]) {
          maxDistance = minDistance[i];
          maxI = i;
        }
    return maxI;
  }
}
