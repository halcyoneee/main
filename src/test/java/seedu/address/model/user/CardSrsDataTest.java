package seedu.address.model.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;

public class CardSrsDataTest {

    private Instant date = Instant.now();
    private CardSrsData testCardData = new CardSrsData(1, 1, 1, date, false);
    private CardSrsData expectedCardData = new CardSrsData(1, 1, 1, date, false);
    private CardSrsData card = new CardSrsData(1, 1, 1, date, false);
    private int outCardInt;

    @Test
    public void setAndGetCardTest() {
        card.setCard(testCardData.getHashCode(), testCardData);
        CardSrsData outCard = card.getCard(1);

        assertEquals(testCardData, outCard);
    }

    @Test
    public void getHashCodeTest() {
        outCardInt = card.getHashCode();

        assertEquals(expectedCardData.getHashCode(), outCardInt);
    }

    @Test
    public void setAndGetNumOfAttemptsTest() {
        card.setNumOfAttempts(testCardData.getNumOfAttempts());
        outCardInt = card.getNumOfAttempts();

        assertEquals(expectedCardData.getNumOfAttempts(), outCardInt);
    }

    @Test
    public void setAndGetStreakTest() {
        card.setStreak(testCardData.getStreak());
        outCardInt = card.getStreak();

        assertEquals(expectedCardData.getStreak(), outCardInt);
    }


    @Test
    public void getSrsDueDateTest() {
        String outCardString = testCardData.getSrsDueDate().toString();
        assertEquals(expectedCardData.getSrsDueDate().toString(), outCardString);
    }
    @Test
    public void equalsTest() {
        CardSrsData card = new CardSrsData(1, 1, 1, date, false);
        CardSrsData cardCopy = new CardSrsData(1, 1, 1, date, false);

        assertFalse(card.equals(cardCopy));

        // same object -> returns true
        assertTrue(card.equals(card));

        // null -> returns false
        assertFalse(card == null);

        // different types -> returns false
        assertFalse(card.equals(5));

        /*
        CardSrsData card1 = new CardSrsData(1, 1, 1, date);
        CardSrsData card2 = new CardSrsData(1, 1, 1, date);
        CardSrsData card3 = new CardSrsData(20, 1, 20, date);
        CardSrsData card4 = new CardSrsData(1, 20, 1, date);
        CardSrsData card5 = new CardSrsData(1, 1, 20, date);
        CardSrsData card6 = new CardSrsData(1, 1, 1, seconds);

        assertTrue("Card is the same", card1.equals(card2));
        assertFalse("Srs date is not the same", card6.equals(card1));
        assertFalse ("Card number of attempts is not the same", card5.equals(card1));
        assertFalse ("Card number of attempts is not the same", card4.equals(card1));
        assertFalse("Card hashcode is not the same", card3.equals(card1));*/

    }
}
