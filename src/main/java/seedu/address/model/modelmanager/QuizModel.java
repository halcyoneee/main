package seedu.address.model.modelmanager;

import java.util.List;

import seedu.address.model.quiz.Quiz;
import seedu.address.model.quiz.QuizCard;
import seedu.address.model.quiz.QuizMode;
import seedu.address.model.quiz.QuizUiDisplayFormatter;
import seedu.address.model.session.Session;
import seedu.address.model.srscard.SrsCard;

/**
 * The API of the QuizModel component.
 */
public interface QuizModel extends Model {

    /**
     * Return mode of {@code session}.
     */
    QuizMode getMode();

    /**
     * Return card count of {@code session}.
     */
    int getCount();

    /**
     * Return name of {@code session}.
     */
    String getName();

    /**
     * Return a list of SrsCards for updating user progress.
     */
    List<SrsCard> getQuizSrsCards();

    /**
     * Return questionHeader of the given lesson.
     */
    String getQuestionHeader();

    /**
     * Return answerHeader of the given lesson.
     */
    String getAnswerHeader();
    /**
     * Sets the {@code Quiz} and {@code Session} information.
     */
    void init(Quiz quiz, Session session);

    /**
     * Returns if there is still card left in {@code Quiz}.
     */
    boolean hasCardLeft();

    /**
     * Returns the next card in line for {@code Quiz}.
     */
    QuizCard getNextCard();

    /**
     * Returns the user current progress in {@code Quiz}.
     */
    String getCurrentProgress();

    /**
     * Returns the current QuizCard in {@code Quiz}.
     */
    QuizCard getCurrentQuizCard();

    /**
     * Update the totalAttempts and streak of a specified card in the current session.
     * @param index of the current {@code QuizCard}
     * @param answer user input
     * @return true if correct
     */
    boolean updateTotalAttemptsAndStreak(int index, String answer);

    /**
     * Returns total attempts in this {@code Quiz}.
     */
    int getQuizTotalAttempts();

    /**
     * Returns the total correct questions attempted in this {code Quiz}.
     */
    int getQuizTotalCorrectQuestions();

    /**
     * Toggles between if the card labeled difficult.
     * @param index of the current {@code QuizCard}
     * @return result after toggling
     */
    boolean toggleIsCardDifficult(int index);

    /**
     * Returns if User is done with {@code Quiz}.
     */
    boolean isQuizDone();

    /**
     * Returns data needed by {@code Session} when {@code Quiz} end.
     */
    List<List<Integer>> end();

    /**
     * Updates user profile after quiz ends.
     * @param quizInformation from quiz.
     */
    void updateUserProfile(List<List<Integer>> quizInformation);

    /**
     * Sets formatter {@code QuizUiDisplayFormatter} in this {@code Quiz}.
     */
    void setDisplayFormatter(QuizUiDisplayFormatter formatter);

    /**
     * Returns formatter {@code QuizUiDisplayFormatter} in this {@code Quiz}.
     */
    QuizUiDisplayFormatter getDisplayFormatter();
}
