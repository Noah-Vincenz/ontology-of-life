import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;
import model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class QuestionProcessorTest {
  private static QuestionProcessor questionProcessor;

  @BeforeEach
  void setUp() throws IOException {
    questionProcessor = new QuestionProcessor(Paths.get("data/ontology.csv"));
    questionProcessor.prepareProcessing();
  }

  @ParameterizedTest
  @CsvSource({
    "hemlock, poisonous, YES",
    "Lassie, four-legged, YES",
    "Luna the Whale, aquatic, YES",
    "sea animal, aquatic, YES"
  })
  void isConsideredToBe(String entity, String attribute, Result expectedResult) {
    assertEquals(
        expectedResult,
        questionProcessor.process("is " + entity + " considered to be " + attribute + "?"));
  }

  @ParameterizedTest
  @CsvSource({
    "Ginger, animal, YES",
    "Lassie, dog, YES",
    // We don't know the answer to this because we don't have any data on pets
    "Lassie, pet, DONT_KNOW",
    // Another example of the incompleteness of our data - from the data we have, we only know that
    // Clifford is an animal, we do not know that he is a dog.
    "Clifford the Big Red Dog, animal, YES",
    "Clifford the Big Red Dog, dog, DONT_KNOW"
  })
  void isInstance(String entity, String parentClass, Result expectedResult) {
    assertEquals(
        expectedResult, questionProcessor.process("is " + entity + " a " + parentClass + "?"));
  }

  @ParameterizedTest
  @CsvSource({
    // We don't know the answer because our data is incomplete - we may be missing a 'SubclassOf'
    // edge between 'pufferfish' and 'mammal', or it may be that this is false - we can't know
    // purely based on our current data
    "pufferfish, mammal, DONT_KNOW",
    "dog, animal, YES"
  })
  void isSubclass(String childClass, String parentClass, Result expectedResult) {
    assertEquals(
        expectedResult,
        questionProcessor.process("is " + childClass + " a type of " + parentClass + "?"));
  }

  @Test
  void testFullOntologyDepth() {
    assertEquals(Result.YES, questionProcessor.process("is Lassie a entity?"));
    assertEquals(Result.YES, questionProcessor.process("is Lassie an entity?"));
  }

  @Test
  void failsGracefullyForUnknownInput() {
    assertEquals(
        Result.DONT_KNOW, questionProcessor.process("is some unknown object a type of animal?"));
    assertEquals(Result.DONT_KNOW, questionProcessor.process("is this true?"));
  }

  @Test
  void testEmptyStringInputAndNull() {
    assertEquals(Result.DONT_KNOW, questionProcessor.process(""));
    assertEquals(Result.DONT_KNOW, questionProcessor.process(null));
  }
}
