package de.uni_leipzig.dbs.entity_resolution.io.reader;

import de.uni_leipzig.dbs.formRepository.dataModel.EntityStructureVersion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by christen on 02.05.2017.
 */
public interface Reader {

  Map<Integer, EntityStructureVersion> read(InputStream is) throws IOException;
}
