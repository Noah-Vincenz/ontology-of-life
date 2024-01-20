from pathlib import Path

from logger import getLogger
from question import Question, QuestionParsingException
from question_result import QuestionResult


class QuestionProcessor:
    logger = getLogger(__name__)

    def __init__(self, ontology_csv: Path):
        # TODO: Load CSV file
        pass

    def process(self, input_question: str) -> QuestionResult:
        try:
            question = Question.from_string(input_question)
            self.logger.info(f"Processing valid question '{input_question}'")
            return self._process_question(question)
        except QuestionParsingException:
            self.logger.exception("Error processing question")
            return QuestionResult.DONT_KNOW

    def _process_question(self, question: Question) -> QuestionResult:
        # TODO: Please fill in your implementation here :)
        #  Feel free to create new methods / classes, import extra libraries etc.
        return QuestionResult.DONT_KNOW
