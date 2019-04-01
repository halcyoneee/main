package seedu.address.testutil;

import static seedu.address.testutil.TypicalCards.CARD_BELGIUM;

import java.time.Instant;

import seedu.address.model.card.Card;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.srscard.SrsCard;
import seedu.address.model.user.CardSrsData;


/**
 * A utility class to help with building {@code SrsCard} objects.
 */
public class SrsCardBuilder {
    public static final CardSrsData DEFAULT_CARDSRSDATA = new CardSrsData(CARD_BELGIUM.hashCode(),
            1, 1, Instant.ofEpochMilli(123), false);
    private Card card;
    private CardSrsData cardData;
    private Lesson lesson;
    /**
     * Empty constructor.
     */
    public SrsCardBuilder() {
        card = CARD_BELGIUM;
        cardData = DEFAULT_CARDSRSDATA;
        lesson = new LessonBuilder().build();
    }

    /**
     * Initializes the SessionBuilder.
     */
    public SrsCardBuilder(SrsCard currentSrsCard) {
        card = currentSrsCard.getCard();
        cardData = currentSrsCard.getCardData();
        lesson = currentSrsCard.getLesson();
    }

    /**
     * Builds and returns a SrsCard.
     * @return a SrsCard
     */
    public SrsCard build() {
        return new SrsCard(card, cardData, lesson);
    }
}
