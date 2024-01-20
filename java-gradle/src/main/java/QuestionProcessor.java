import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionProcessor {
  private static final Logger logger = LoggerFactory.getLogger(QuestionProcessor.class);

  public QuestionProcessor(Path ontologyCsv) {
    // TODO: Load CSV file
  }

  public enum Result {
    YES,
    NO,
    DONT_KNOW
  }

  public Result process(String inputQuestion) {
    try {
      Question question = Question.fromString(inputQuestion);
      logger.info("Processing valid question '{}'", inputQuestion);
      return processQuestion(question);
    } catch (QuestionParsingException e) {
      logger.error("Error processing question: " + e.getMessage());
      return Result.DONT_KNOW;
    }
  }

  private Result processQuestion(Question question) {

    // TODO: Please fill in your implementation here :)
    //  Feel free to create new methods / classes, import extra libraries etc.

    return Result.DONT_KNOW;
  }
}
