# Java Version

This directory holds the Java version of this take-home project, built with Gradle.

## Testing

Tests have been set up in this directory. To run the tests, use `./gradlew clean test` (or equivalent from within your
IDE of choice).

To control which tests are executed, use the `--tests` option, e.g.
`./gradlew clean test --tests QuestionProcessorTest.gingerIsAnAnimal`.

Tests can also be run using GitHub actions if you like. This can be done by following these steps:

* Push your code to GitHub: `git push`
* Go to the 'Actions' tab
* Click on the 'Test' workflow
* Click 'Run Workflow' and select your branch from the dropdown.

## Linting

Linting has been set up in this directory. To lint your code, use `./gradlew spotlessJavaApply`.
