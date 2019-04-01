package seedu.address.model.session;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import seedu.address.model.quiz.QuizCard;
import seedu.address.model.quiz.QuizMode;
import seedu.address.model.srscard.SrsCard;
import seedu.address.model.user.CardSrsData;


/**
 * Represents a session that stores cards based on srs data.
 */
public class Session {
    public static final int CARD_COUNT_MINIMUM = 1;

    private String name;
    private QuizMode mode;
    private int cardCount;
    private List<QuizCard> quizCards;
    private List<SrsCard> srsCards;

    public Session(String name, int cardCount, QuizMode mode) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (cardCount < CARD_COUNT_MINIMUM) {
            throw new IllegalArgumentException("CardCount should not zero");
        }
        if ((mode != QuizMode.LEARN) & (mode != QuizMode.REVIEW) & (mode != QuizMode.PREVIEW)
            & (mode != QuizMode.DIFFICULT)) {
            throw new IllegalArgumentException("Invalid mode");
        }
        this.name = name;
        this.cardCount = cardCount;
        this.mode = mode;
    }

    public Session(String name, int cardCount, QuizMode mode, List<SrsCard> srsCards) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (cardCount < CARD_COUNT_MINIMUM) {
            throw new IllegalArgumentException("CardCount should not be less than five in a single session");
        }
        if ((mode != QuizMode.LEARN) & (mode != QuizMode.REVIEW) & (mode != QuizMode.PREVIEW)
            & (mode != QuizMode.DIFFICULT)) {
            throw new IllegalArgumentException("Invalid mode");
        }

        this.name = name;
        this.cardCount = cardCount;
        this.mode = mode;
        this.srsCards = srsCards;
    }

    public Session(String name, QuizMode mode, List<SrsCard> srsCards) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid name");
        }
        if ((mode != QuizMode.LEARN) & (mode != QuizMode.REVIEW) & (mode != QuizMode.PREVIEW)
            & (mode != QuizMode.DIFFICULT)) {
            throw new IllegalArgumentException("Invalid mode");
        }

        this.name = name;
        this.cardCount = 10;
        this.mode = mode;
        this.srsCards = srsCards;
    }

    /**
     * Generate a list of quizCards that will pass to quiz system.
     */
    public List<QuizCard> generateSession() {
        SrsCard currentCard;
        quizCards = new ArrayList<>();

        for (int i = 0; i < cardCount; i++) {
            currentCard = srsCards.get(i);
            quizCards.add(new QuizCard(currentCard.getQuestion(), currentCard.getAnswer(),
                    currentCard.getHints(), currentCard.getQuestionHeader(), currentCard.getAnswerHeader()));
        }
        return quizCards;
    }

    public QuizMode getMode() {
        return mode;
    }

    public int getCount() {
        return cardCount;
    }
    public void setCount(int count) {
        cardCount = count;
    }

    public String getName() {
        return name;
    }

    public List<SrsCard> getSrsCards() {
        return srsCards;
    }
    public List<SrsCard> getQuizSrsCards() {
        List<SrsCard> quizSrsCards = new ArrayList<>();
        for (int i = 0; i < cardCount; i++) {
            quizSrsCards.add(srsCards.get(i));
        }
        return quizSrsCards;
    }
    public String getQuestionHeader() {
        SrsCard srsCard = srsCards.get(0);
        return srsCard.getQuestionHeader();
    }
    public String getAnswerHeader() {
        SrsCard srsCard = srsCards.get(0);
        return srsCard.getAnswerHeader();
    }

    /**
     * Returns user profile after quiz ends.
     * @param quizInformation from quiz.
     */
    public List<CardSrsData> updateUserProfile(List<List<Integer>> quizInformation) {
        Instant currentDate = Instant.now();
        SrsCardsManager updateManager = new SrsCardsManager(this.getQuizSrsCards(), quizInformation, currentDate);
        return updateManager.updateCardData();
    }
}
