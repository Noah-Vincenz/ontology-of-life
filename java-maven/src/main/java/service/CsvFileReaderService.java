package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvFileReaderService {

  private final BufferedReader reader;
  private final ReversedLinesFileReader reverseReader;
  private static final Logger logger = LoggerFactory.getLogger(CsvFileReaderService.class);
  private final String ontologyCsvPathString;

  public CsvFileReaderService(Path ontologyCsvPath) throws IOException {
    reader = Files.newBufferedReader(ontologyCsvPath);
    reverseReader = ReversedLinesFileReader.builder().setPath(ontologyCsvPath).get();
    ontologyCsvPathString = ontologyCsvPath.toString();
  }

  /** Method to get ontology entities from the linked csv file path */
  public List<String> getOntologyEntitiesFromCsvFile() {
    try {
      // read last line of file to identify array list max size to avoid array resizing allowing us
      // to optimise performance
      int ontologySize = getOntologySizeByReadingLastLine();
      List<String> entityListFromOntology = new ArrayList<>(ontologySize) {};
      String line;
      reader.readLine(); // skip first line = csv header
      while ((line = reader.readLine()) != null) {
        logger.debug("Reading [{}]", line);
        // split by comma to get terms - this assumes we always have the same order
        String[] ontologyEntry = line.split(",");
        String edgeType = ontologyEntry[1];
        String headEntity = ontologyEntry[2];
        String tailEntity = ontologyEntry[3];
        entityListFromOntology.add(edgeType + "," + headEntity + "," + tailEntity);
      }
      return entityListFromOntology;
    } catch (IOException e) {
      logger.error(
          "IOException occurred while reading file from path [{}]", ontologyCsvPathString, e);
      e.printStackTrace();
    }
    return Collections.emptyList();
  }

  private int getOntologySizeByReadingLastLine() throws IOException {
    String line = reverseReader.readLine();
    reverseReader.close();
    // split by comma to get row = max number of entities in ontology
    return Integer.parseInt(line.split(",")[0]);
  }
}
