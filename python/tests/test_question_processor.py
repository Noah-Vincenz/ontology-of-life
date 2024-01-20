from pathlib import Path

import pytest

from question_processor import QuestionProcessor
from question_result import QuestionResult


@pytest.fixture
# The path returned will be used as the data file for all tests in this scope
def question_processor_data_file() -> Path:
    return Path("data/ontology.csv")


@pytest.mark.parametrize(
    ["entity", "attribute", "expected_result"],
    [
        ("hemlock", "poisonous", QuestionResult.YES),
        ("Lassie", "four-legged", QuestionResult.YES),
    ],
)
def test_is_considered_to_be(
    entity: str,
    attribute: str,
    expected_result: QuestionResult,
    question_processor: QuestionProcessor,
) -> None:
    assert expected_result == question_processor.process(
        f"is {entity} considered to be {attribute}?"
    )


@pytest.mark.parametrize(
    ["entity", "parent_class", "expected_result"],
    [
        ("Ginger", "animal", QuestionResult.YES),
        # We don't know the answer to this because we don't have any data on pets
        ("Lassie", "pet", QuestionResult.DONT_KNOW),
        # Another example of the incompleteness of our data - from the data we have, we only know that Clifford is an
        # animal, we do not know that he is a dog.
        ("Clifford the Big Red Dog", "animal", QuestionResult.YES),
        ("Clifford the Big Red Dog", "dog", QuestionResult.DONT_KNOW),
    ],
)
def test_is_instance(
    entity: str,
    parent_class: str,
    expected_result: QuestionResult,
    question_processor: QuestionProcessor,
) -> None:
    assert expected_result == question_processor.process(
        f"is {entity} a {parent_class}?"
    )


@pytest.mark.parametrize(
    ["child_class", "parent_class", "expected_result"],
    [
        # We don't know the answer because our data is incomplete - we may be missing a 'SubclassOf' edge between
        # 'pufferfish' and 'mammal', or it may be that this is false - we can't know purely based on our current data
        ("pufferfish", "mammal", QuestionResult.DONT_KNOW),
    ],
)
def test_is_subclass(
    child_class: str,
    parent_class: str,
    expected_result: QuestionResult,
    question_processor: QuestionProcessor,
) -> None:
    assert expected_result == question_processor.process(
        f"is {child_class} a type of {parent_class}?"
    )


# add more tests below here :)
