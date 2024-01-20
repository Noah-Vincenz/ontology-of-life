# Java Version

This directory holds the Java version of this take-home project, built with Maven.

## Testing

Tests have been set up in this directory. To run the tests, use `mvn test` (or equivalent from within your IDE of 
choice).

To control which tests are executed, pass the `test` property, e.g.
`mvn -Dtest=QuestionProcessorTest#gingerIsAnAnimal test`.

Tests can also be run using GitHub actions if you like. This can be done by following these steps:

* Push your code to GitHub: `git push`
* Go to the 'Actions' tab
* Click on the 'Test' workflow
* Click 'Run Workflow' and select your branch from the dropdown.

## Linting

Linting has been set up in this directory. To lint your code, use `mvn com.coveo:fmt-maven-plugin:format`.

### Future improvements

- smarter grammar over regex -> implement DSL and parser
- could make use of external OWL API
- store ontologies externally
- create API to run processing
