package seedu.address.model.srscard;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Instant;
import java.util.List;

import seedu.address.model.card.Card;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.user.CardSrsData;

/**
 * Represents a flashcard contains both core fields(question and answer) and srs data.
 */
public class SrsCard {
    private Card card;
    private CardSrsData cardData;
    private Lesson lesson;
    private int questionIndex;
    private int answerIndex;

    public SrsCard(Card card, CardSrsData cardData, Lesson lesson) {
        requireAllNonNull(card, cardData, lesson);
        this.card = card;
        this.cardData = cardData;
        this.lesson = lesson;

        questionIndex = lesson.getQuestionCoreIndex();
        answerIndex = lesson.getAnswerCoreIndex();
    }
    public String getQuestion() {
        return card.getCore(questionIndex);
    }

    public Card getCard() {
        return card;
    }

    public CardSrsData getCardData() {
        return cardData;
    }

    public String getAnswer() {
        return card.getCore(answerIndex);
    }

    public Instant getSrsDueDate() {
        return cardData.getSrsDueDate();
    }

    public int getHashcode() {
        return cardData.getHashCode();
    }

    public int getNumOfAttempts() {
        return cardData.getNumOfAttempts();
    }

    public int getStreak() {
        return cardData.getStreak();
    }
    public List<String> getHints() {
        return card.getOptionals();
    }
    public String getQuestionHeader() {
        return lesson.getCoreHeaders().get((lesson.getQuestionCoreIndex()));
    }
    public String getAnswerHeader() {
        return lesson.getCoreHeaders().get((lesson.getAnswerCoreIndex()));
    }

    public Lesson getLesson() {
        return lesson;
    }
    @Override
    public boolean equals(Object obj) {
        SrsCard other = (SrsCard) obj;
        return other.getHashcode() == this.getHashcode();
    }
}
