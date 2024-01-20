import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionProcessorLargeTest {
  private static QuestionProcessor questionProcessor;

  @BeforeEach
  void setUp() {
    questionProcessor = new QuestionProcessor(Paths.get("data/ontology-large.csv"));
  }

  @Test
  void babyGrandIsATypeOfMusicalInstrument() {
    assertEquals(
        QuestionProcessor.Result.YES,
        questionProcessor.process("is baby grand a type of musical instrument?"));
  }

  @Test
  void smirnoffIsADrink() {
    assertEquals(QuestionProcessor.Result.YES, questionProcessor.process("is Smirnoff a drink?"));
  }

  @Test
  void cheddarIsHard() {
    assertEquals(
        QuestionProcessor.Result.YES,
        questionProcessor.process("is Cheddar considered to be hard?"));
  }
}
