import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;
import model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionProcessorLargeTest {
  private static QuestionProcessor questionProcessor;

  @BeforeEach
  void setUp() throws IOException {
    questionProcessor = new QuestionProcessor(Paths.get("data/ontology-large.csv"));
    questionProcessor.prepareProcessing();
  }

  @Test
  void babyGrandIsATypeOfMusicalInstrument() {
    assertEquals(
        Result.YES, questionProcessor.process("is baby grand a type of musical instrument?"));
  }

  @Test
  void smirnoffIsADrink() {
    assertEquals(Result.YES, questionProcessor.process("is Smirnoff a drink?"));
  }

  @Test
  void cheddarIsHard() {
    assertEquals(Result.YES, questionProcessor.process("is Cheddar considered to be hard?"));
  }

  @Test
  void babyGrandIsATypeOfInstrument() {
    assertEquals(Result.YES, questionProcessor.process("is baby grand a type of instrument?"));
  }
}
