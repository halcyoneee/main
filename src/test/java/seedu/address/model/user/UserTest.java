package seedu.address.model.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.HashMap;

import org.junit.Test;

public class UserTest {
    private User user = new User();
    private Instant date = Instant.now();
    private final CardSrsData testCardData = new CardSrsData(1, 1, 1, date, false);
    private final CardSrsData testCardData2 = new CardSrsData(20, 20, 20, date,
            false);

    @Test
    public void addCardTest() {
        user.addCard(testCardData);
        CardSrsData expectedOutput = user.getCard(testCardData.getHashCode());
        assertEquals(testCardData, expectedOutput);
    }

    @Test
    public void deleteCardTest() {
        user.deleteCard(testCardData);
        CardSrsData expectedOutput = user.getCard(testCardData.getHashCode());
        assertEquals(null, expectedOutput);
    }

    @Test
    public void setCardTest() {
        user.setCard(testCardData);
        CardSrsData expectedOutput = user.getCard(testCardData.getHashCode());
        assertEquals(testCardData, expectedOutput);
    }

    @Test
    public void getCardTest() {
        user.addCard(testCardData);
        CardSrsData expectedOutput = user.getCard(testCardData.getHashCode());
        assertEquals(testCardData.getHashCode(), expectedOutput.getHashCode());
    }

    @Test
    public void getCardsTest() {
        HashMap<Integer, CardSrsData> expectedOutput = new HashMap<>();
        expectedOutput.put(1, testCardData);
        expectedOutput.put(20, testCardData2);
        user.setCard(testCardData);
        user.setCard(testCardData2);
        assertEquals(user.getCards(), expectedOutput);
    }

    @Test
    public void equalTest() {
        User user1 = new User();
        user1.addCard(testCardData);
        User user2 = new User();
        user2.addCard(testCardData);
        User user3 = new User();
        user3.addCard(testCardData2);

        assertTrue("User is the same", user1.equals(user2));
        assertFalse("User is not the same", user3.equals(user1));
        assertTrue("User is the same", user1.equals(user1));
        assertFalse("Compare with different objects", user.equals(testCardData));

    }
}
