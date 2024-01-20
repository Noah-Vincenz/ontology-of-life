import exception.QuestionParsingException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import model.Question;
import model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CsvFileReaderService;
import service.QuestionProcessorService;

public class QuestionProcessor {

  private final CsvFileReaderService readerService;
  private final QuestionProcessorService processorService;
  private static final Logger logger = LoggerFactory.getLogger(QuestionProcessor.class);
  private List<String> entityListFromOntology;

  public QuestionProcessor(Path ontologyCsvPath) throws IOException {
    readerService = new CsvFileReaderService(ontologyCsvPath);
    processorService = new QuestionProcessorService();
  }

  /** Method to prepare any ontology file processing to avoid unnecessary repeated processing. */
  public void prepareProcessing() {
    entityListFromOntology = readerService.getOntologyEntitiesFromCsvFile();
  }

  public Result process(String inputQuestion) {
    if (entityListFromOntology == null || entityListFromOntology.isEmpty()) {
      logger.error(
          "Entity list from ontology has not been populated. Please call QuestionProcessor::prepareProcessing before calling QuestionProcessor::process");
      return Result.DONT_KNOW;
    }
    try {
      Question question = Question.fromString(inputQuestion);
      logger.info("Processing valid question '{}'", inputQuestion);
      return processorService.processQuestion(question, entityListFromOntology);
    } catch (QuestionParsingException e) {
      logger.error("Error processing question: {}", e.getMessage());
      return Result.DONT_KNOW;
    }
  }
}
