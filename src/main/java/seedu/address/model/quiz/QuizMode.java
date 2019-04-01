package seedu.address.model.quiz;

/**
 * Different types of mode supported in Quiz.
 * Learn: sees both the question and answer first then get tested.
 * Review: only get tested.
 * Preview: sees both question and answer but not tested.
 * Difficult: sees cards labelled as difficult with both question and answer.
 */
public enum QuizMode {
    LEARN,
    REVIEW,
    PREVIEW,
    DIFFICULT
}
