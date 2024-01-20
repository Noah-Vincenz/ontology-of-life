package service;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;
import model.Question;
import model.Result;

public class QuestionProcessorService {

  /**
   * Process a given question using a given list of entities from an ontology.
   * Using {@link ArrayDeque} over {@link java.util.ArrayList} here as ArrayDeque doesn't have the overhead of shifting the array contents left on remove that ArrayList has.
   * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ArrayDeque.html">docs.oracle</a> and <a href="https://publicobject.com/2010/07/07/caliper_confirms_reality_linked_list_vs_array_list/">publicobject blog post</a>.
   * Testing the performance I managed to get ArrayDeque to be 33% faster than ArrayList.
   */
  public Result processQuestion(Question question, List<String> entityListFromOntology) {
    String questionHead = question.head();
    String questionTail = question.tail();
    Question.Type questionType = question.type();
    String ontologyItemToLookFor = questionType.toName() + "," + questionHead + "," + questionTail;
    // questionHead INSTANCE_OF questionTail OR questionHead SUBCLASS_OF questionTail OR
    // questionHead HAS_ATTRIBUTE questionTail
    if (entityListFromOntology.contains(ontologyItemToLookFor)) {
      return Result.YES;
    }
    // otherwise create list of all elems that start with questionHead and contain questionType to
    // list and start BFS
    if (questionType.equals(Question.Type.INSTANCE_OF)) {
      // a is instance of b directly and b is subclass of c, so a is also instance of c
      // a) questionHead INSTANCE_OF b
      // b) b SUBCLASS questionTail
      ArrayDeque<String> itemsToCheck =
          entityListFromOntology.stream()
              .filter(
                  it -> it.matches(Question.Type.INSTANCE_OF.toName() + "," + questionHead + ",.+"))
              .collect(Collectors.toCollection(ArrayDeque::new));
      return findResultFromBFS(
          itemsToCheck, Question.Type.SUBCLASS_OF, questionTail, entityListFromOntology);
    } else if (questionType.equals(Question.Type.SUBCLASS_OF)) {
      // a is subclass of b and b is subclass of c, so a is subclass of c
      // a) questionHead SUBCLASS_OF b
      // b) b SUBCLASS_OF questionTail (unlimited depth)
      ArrayDeque<String> itemsToCheck =
          entityListFromOntology.stream()
              .filter(
                  it -> it.matches(Question.Type.SUBCLASS_OF.toName() + "," + questionHead + ",.+"))
              .collect(Collectors.toCollection(ArrayDeque::new));
      return findResultFromBFS(
          itemsToCheck, Question.Type.SUBCLASS_OF, questionTail, entityListFromOntology);
    } else { // questionType = HAS_ATTRIBUTE
      // a is instance of b and b has attribute c, so a also has attribute c
      // a) questionHead INSTANCE_OF b
      // b) b HAS_ATTRIBUTE questionTail
      ArrayDeque<String> itemsToCheck =
          entityListFromOntology.stream()
              .filter(
                  it -> it.matches(Question.Type.INSTANCE_OF.toName() + "," + questionHead + ",.+"))
              .collect(Collectors.toCollection(ArrayDeque::new));
      return findResultFromBFS(
          itemsToCheck, Question.Type.HAS_ATTRIBUTE, questionTail, entityListFromOntology);
    }
  }

  /**
   * Find the {@link Result} from running breadth-first-search. Remove the first element,
   * current_elem, from the front of the given list of items to check and check if we have reached
   * our destination element (matching our given destinationTail). Otherwise, append all next
   * elements starting with the tail of our current_elem as head and contain SUBCLASS_OF to the back
   * of our list. Repeat while our given list of items to check is not empty, or we have found our
   * destination element.
   *
   * @return {@link Result} specifying whether our search was successful.
   */
  private Result findResultFromBFS(
      ArrayDeque<String> itemsToCheck,
      Question.Type type,
      String destinationTail,
      List<String> entityListFromOntology) {
    while (!itemsToCheck.isEmpty()) {
      String firstElemTail = itemsToCheck.removeFirst().split(",")[2];
      if (entityListFromOntology.contains(
          type.toName() + "," + firstElemTail + "," + destinationTail)) {
        return Result.YES; // DONE
      }
      entityListFromOntology.stream()
          .filter(
              it -> it.matches(Question.Type.SUBCLASS_OF.toName() + "," + firstElemTail + ",.+"))
          .forEach(itemsToCheck::add);
    }
    return Result.DONT_KNOW;
  }
}
