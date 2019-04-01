package seedu.address.model.quiz;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a quiz that stores a list of QuizCard
 */
public class Quiz {

    public static final String MESSAGE_CONSTRAINTS = "QuizMode must only be learn/review/preview";

    private List<QuizCard> currentSession;
    private List<QuizCard> generatedSession;
    private QuizMode mode;
    private QuizCard currentQuizCard;
    private int currentCardIndex;
    private int generatedCardSize;
    private boolean isQuizDone;
    private int quizTotalAttempts;
    private int quizTotalCorrectQuestions;

    /**
     * Build constructor from session
     * @param session contains a list of question, answer and list of optional
     */
    public Quiz(List<QuizCard> session, QuizMode mode) {
        requireNonNull(session);
        checkArgument(mode != null, MESSAGE_CONSTRAINTS);

        this.currentSession = session;
        this.mode = mode;
        this.currentCardIndex = -1;
        this.generatedCardSize = -1;
        this.isQuizDone = false;
        this.quizTotalAttempts = 0;
        this.quizTotalCorrectQuestions = 0;

        generate();
    }

    /**
     * Generates a list of cards based on the chosen cards given by session.
     * R
     */
    public List<QuizCard> generate() {
        generatedSession = new ArrayList<>();

        switch (mode) {
        case PREVIEW:
            generatePreview();
            break;
        case LEARN:
            // Learn is a combination of Preview + Review
            generatePreview();
            generateReview();
            break;
        case REVIEW:
            generateReview();
            break;
        default:
            break;
        }
        if (mode.equals(QuizMode.DIFFICULT)) {
            generatePreview();
        }

        generatedCardSize = generatedSession.size();
        return generatedSession;
    }

    /**
     * Generates a list of card with the mode Review
     */
    private void generateReview() {
        QuizCard currentCard;
        for (int i = 0; i < currentSession.size(); i++) {
            currentCard = currentSession.get(i);
            generatedSession.add(new QuizCard(i, currentCard.getQuestion(), currentCard.getAnswer(), QuizMode.REVIEW));
        }

        for (int i = 0; i < currentSession.size(); i++) {
            currentCard = currentSession.get(i);
            generatedSession.add(new QuizCard(i, currentCard.getAnswer(), currentCard.getQuestion(), QuizMode.REVIEW));
        }
    }

    /**
     * Generates a list of card with the mode Preview see but don't need to answer.
     */
    private void generatePreview() {
        QuizCard currentCard;

        for (int i = 0; i < currentSession.size(); i++) {
            currentCard = currentSession.get(i);
            generatedSession.add(new QuizCard(i, currentCard.getQuestion(), currentCard.getAnswer(), QuizMode.PREVIEW));
        }
    }

    /**
     * Returns true if there is card left in quiz.
     */
    public boolean hasCardLeft() {
        return currentCardIndex < (generatedCardSize - 1);
    }

    /**
     * Returns the next card in line.
     */
    public QuizCard getNextCard() {
        currentCardIndex++;

        if (currentCardIndex < generatedCardSize) {
            currentQuizCard = generatedSession.get(currentCardIndex);
            return currentQuizCard;
        }

        throw new IndexOutOfBoundsException("No cards left.");
    }

    public String getCurrentProgress() {
        return (currentCardIndex + 1) + "/" + generatedCardSize;
    }

    public QuizCard getCurrentQuizCard() {
        requireNonNull(currentQuizCard);
        return currentQuizCard;
    }

    /**
     * Updates the totalAttempts and streak of a specified card in the current session
     * and current quiz session totalAttempts and totalCorrectQuestions
     * @param index of the card
     * @param answer user input
     */
    public boolean updateTotalAttemptsAndStreak(int index, String answer) {
        QuizCard sessionCard = currentSession.get(index);
        boolean isCorrect = currentQuizCard.isCorrect(answer);
        sessionCard.updateTotalAttemptsAndStreak(isCorrect);

        if (isCorrect) {
            quizTotalCorrectQuestions++;
        }

        quizTotalAttempts++;

        return isCorrect;
    }

    public int getQuizTotalAttempts() {
        return quizTotalAttempts;
    }

    public int getQuizTotalCorrectQuestions() {
        return quizTotalCorrectQuestions;
    }

    /**
     * Toggles between if the card labeled difficult.
     * @param index of the card
     * @return result after toggling
     */
    public boolean toggleIsCardDifficult(int index) {
        QuizCard sessionCard = currentSession.get(index);
        sessionCard.toggleIsCardDifficult();

        return sessionCard.isCardDifficult();
    }

    /**
     * Returns the current session.
     */
    public List<QuizCard> getCurrentSession() {
        return currentSession;
    }

    /**
     * Format data needed by Session
     * @return a list of index of card, total attempts, streak and isDifficult in this session.
     */
    public List<List<Integer>> end() {
        this.isQuizDone = true;

        List<List<Integer>> session = new ArrayList<>();
        QuizCard card;
        for (int i = 0; i < currentSession.size(); i++) {
            card = currentSession.get(i);
            int isCardDifficult = card.isCardDifficult() ? 1 : 0;
            session.add(Arrays.asList(i, card.getTotalAttempts(), card.getStreak(), isCardDifficult));
        }

        return session;
    }

    public boolean isQuizDone() {
        return isQuizDone;
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof Quiz)) {
            return false;
        }

        // state check
        Quiz other = (Quiz) obj;
        return other.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentSession, generatedSession, mode,
            currentQuizCard, currentCardIndex, isQuizDone);
    }

}
