package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.thread;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Particle;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by christen on 05.07.2018.
 */
public class ParticlePartioning {


  public static List<List<Map.Entry<Long, Particle>>> partionParticleSet(Long2ObjectMap<Particle> particleMap, int threadNumber) {
    List<List<Map.Entry<Long, Particle>>> list = new ArrayList<>();

    List<Map.Entry<Long, Particle>> globalList = new ArrayList<>(particleMap.long2ObjectEntrySet());
    for (int i = 0; i < threadNumber; i++) {
      list.add(new ArrayList<>());
    }
    for (int i = 0; i < globalList.size(); i++) {
      list.get(i%threadNumber).add(globalList.get(i));
    }
    return list;
  }
}
