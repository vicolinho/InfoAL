package de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.io;

import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Particle;
import de.uni_leipzig.dbs.entity_resolution.machine_learning.active.swarm.Swarm;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import weka.core.Instance;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by christen on 06.03.2018.
 */
public class SwarmWriter {

  public void writeCurrentSwarm(int iteration, Long2ObjectMap<Swarm> tpSwarm, Long2ObjectMap<Swarm> tnSwarm,
                                Long2ObjectMap<List<Particle>> fpParticles, Long2ObjectMap<List<Particle>> fnParticles,
                                Long2ObjectMap<Instance> drawnParticles) {
    try {
      FileWriter fw = new FileWriter("./er/swarm/"+"movement"+iteration+".csv");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
