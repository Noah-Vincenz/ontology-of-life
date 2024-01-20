import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Question(Type type, String head, String tail) {
  public enum Type {
    SUBCLASS_OF(Pattern.compile("is (.*) a type of (.*)\\?")),
    INSTANCE_OF(Pattern.compile("is (.*) (?:a|an) (.*)\\?")),
    HAS_ATTRIBUTE(Pattern.compile("is (.*) considered to be (.*)\\?"));

    private final Pattern pattern;

    Type(Pattern pattern) {
      this.pattern = pattern;
    }
  }

  public static Question fromString(String question) throws QuestionParsingException {
    for (Type questionType : Type.values()) {
      Matcher matcher = questionType.pattern.matcher(question);
      if (matcher.matches()) {
        return new Question(questionType, matcher.group(1), matcher.group(2));
      }
    }
    throw new QuestionParsingException(String.format("Unable to parse question: %s", question));
  }
}
