import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class QuestionProcessorTest {
  private static QuestionProcessor questionProcessor;

  @BeforeEach
  void setUp() {
    questionProcessor = new QuestionProcessor(Paths.get("data/ontology.csv"));
  }

  @ParameterizedTest
  @CsvSource({"hemlock, poisonous, YES", "Lassie, four-legged, YES"})
  void isConsideredToBe(String entity, String attribute, QuestionProcessor.Result expectedResult) {
    assertEquals(
        expectedResult,
        questionProcessor.process("is " + entity + " considered to be " + attribute + "?"));
  }

  @ParameterizedTest
  @CsvSource({
    "Ginger, animal, YES",
    // We don't know the answer to this because we don't have any data on pets
    "Lassie, pet, DONT_KNOW",
    // Another example of the incompleteness of our data - from the data we have, we only know that
    // Clifford is an animal, we do not know that he is a dog.
    "Clifford the Big Red Dog, animal, YES",
    "Clifford the Big Red Dog, dog, DONT_KNOW"
  })
  void isInstance(String entity, String parentClass, QuestionProcessor.Result expectedResult) {
    assertEquals(
        expectedResult, questionProcessor.process("is " + entity + " a " + parentClass + "?"));
  }

  @ParameterizedTest
  @CsvSource({
    // We don't know the answer because our data is incomplete - we may be missing a 'SubclassOf'
    // edge between 'pufferfish' and 'mammal', or it may be that this is false - we can't know
    // purely based on our current data
    "pufferfish, mammal, DONT_KNOW"
  })
  void isSubclass(String childClass, String parentClass, QuestionProcessor.Result expectedResult) {
    assertEquals(
        expectedResult,
        questionProcessor.process("is " + childClass + " a type of " + parentClass + "?"));
  }

  // Add more tests below here :)
}
