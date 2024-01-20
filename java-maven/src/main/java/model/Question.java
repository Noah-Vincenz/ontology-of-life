package model;

import exception.QuestionParsingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Question(Type type, String head, String tail) {
  public enum Type {
    SUBCLASS_OF(Pattern.compile("is (.*) a type of (.*)\\?")),
    INSTANCE_OF(Pattern.compile("is (.*) (?:a|an) (.*)\\?")),
    HAS_ATTRIBUTE(Pattern.compile("is (.*) considered to be (.*)\\?"));

    private final Pattern pattern;

    /**
     * Function to convert a given Type enum to the String format received in our ontologies.
     *
     * @return the String type received in our ontologies matching a given input Type enum.
     */
    public String toName() {
      if (this.equals(SUBCLASS_OF)) {
        return "SubclassOf";
      } else if (this.equals(INSTANCE_OF)) {
        return "InstanceOf";
      } else { // HAS_ATTRIBUTE
        return "HasAttribute";
      }
    }

    Type(Pattern pattern) {
      this.pattern = pattern;
    }
  }

  public static Question fromString(String question) throws QuestionParsingException {
    if (question == null || question.isEmpty())
      throw new QuestionParsingException(
          String.format(
              "Unable to parse null/emptyString question. Please provide question in one of the following formats: '%s' OR '%s' OR '%s'",
              Type.SUBCLASS_OF.pattern.toString(),
              Type.INSTANCE_OF.pattern.toString(),
              Type.HAS_ATTRIBUTE.pattern.toString()));
    for (Type questionType : Type.values()) {
      Matcher matcher = questionType.pattern.matcher(question);
      if (matcher.matches()) {
        return new Question(questionType, matcher.group(1), matcher.group(2));
      }
    }
    throw new QuestionParsingException(
        String.format(
            "Unable to parse question [%s]. Please provide question in one of the following formats: '%s' OR '%s' OR '%s'",
            question,
            Type.SUBCLASS_OF.pattern.toString(),
            Type.INSTANCE_OF.pattern.toString(),
            Type.HAS_ATTRIBUTE.pattern.toString()));
  }
}
