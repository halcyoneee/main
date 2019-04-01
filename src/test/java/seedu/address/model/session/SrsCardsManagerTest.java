package seedu.address.model.session;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.SrsCardBuilder.DEFAULT_CARDSRSDATA;
import static seedu.address.testutil.TypicalCards.CARD_BELGIUM;
import static seedu.address.testutil.TypicalCards.CARD_CAT;
import static seedu.address.testutil.TypicalCards.CARD_DOG;
import static seedu.address.testutil.TypicalCards.CARD_DOGCAT;
import static seedu.address.testutil.TypicalCards.CARD_JAPAN;
import static seedu.address.testutil.TypicalCards.CARD_MULTI;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import seedu.address.model.lesson.Lesson;
import seedu.address.model.srscard.SrsCard;
import seedu.address.model.user.CardSrsData;
import seedu.address.testutil.Assert;
import seedu.address.testutil.LessonBuilder;
import seedu.address.testutil.SrsCardBuilder;

public class SrsCardsManagerTest {
    private Lesson lesson = new LessonBuilder().build();
    private HashMap<Integer, CardSrsData> cardData = new HashMap<>();
    private List<List<Integer>> quizInformation = new ArrayList<>();
    private List<SrsCard> srsCards = List.of(new SrsCardBuilder().build(),
            new SrsCardBuilder(new SrsCard(CARD_JAPAN, new CardSrsData(CARD_JAPAN.hashCode(), 1, 1,
                    Instant.now().plus(Duration.ofHours(20)), false), lesson)).build(),
            new SrsCardBuilder(new SrsCard(CARD_CAT, new CardSrsData(CARD_CAT.hashCode(), 1,
                    1, Instant.now().plus(Duration.ofHours(7)), false), lesson)).build(),
            new SrsCardBuilder(new SrsCard(CARD_DOG, new CardSrsData(CARD_DOG.hashCode(),
                    1, 1, Instant.now().plus(Duration.ofHours(3)), false), lesson)).build(),
            new SrsCardBuilder(new SrsCard(CARD_DOGCAT, new CardSrsData(CARD_DOGCAT.hashCode(),
                    1, 0, Instant.now().plus(Duration.ofHours((long) 0.5)), false), lesson)).build(),
            new SrsCardBuilder(new SrsCard(CARD_MULTI, new CardSrsData(CARD_MULTI.hashCode(),
                    1, 1, Instant.now().plus(Duration.ofHours(0)), false), lesson)).build());

    @Test
    public void constructor_sort_throwsNullPointerException() {
        cardData.put(CARD_BELGIUM.hashCode(), DEFAULT_CARDSRSDATA);
        Assert.assertThrows(NullPointerException.class, () ->
                new SrsCardsManager(lesson, null));
        Assert.assertThrows(NullPointerException.class, () ->
                new SrsCardsManager(null, cardData));
    }
    @Test
    public void constructor_update_throwsNullPointerException() {
        Instant currentDate = Instant.now();
        Assert.assertThrows(NullPointerException.class, () ->
                new SrsCardsManager(null, quizInformation, currentDate));
        Assert.assertThrows(NullPointerException.class, () ->
                new SrsCardsManager(srsCards, null, currentDate));
    }
    @Test
    public void checkSort() {
        cardData.put(CARD_BELGIUM.hashCode(), DEFAULT_CARDSRSDATA);
        CardSrsData cardSrsDataJapan = new CardSrsData(CARD_JAPAN.hashCode(), 1, 1,
                Instant.ofEpochMilli(1233), false);
        cardData.put(CARD_JAPAN.hashCode(), cardSrsDataJapan);
        lesson.addCard(CARD_CAT);
        CardSrsData cardSrsDataCat = new CardSrsData(CARD_CAT.hashCode(), 1, 1,
                Instant.now(), false);
        SrsCardsManager currentManager = new SrsCardsManager(lesson, cardData);
        List<SrsCard> srsCards = currentManager.sort();
        List<SrsCard> expected = new ArrayList<>();
        expected.add(new SrsCardBuilder().build());
        expected.add(new SrsCard(CARD_JAPAN, cardSrsDataJapan, lesson));
        expected.add(new SrsCard(CARD_CAT, cardSrsDataCat, lesson));
        assertEquals(expected, srsCards);
    }
    @Test
    public void checkSortReverse() {
        cardData.put(CARD_BELGIUM.hashCode(), DEFAULT_CARDSRSDATA);
        CardSrsData cardSrsDataJapan = new CardSrsData(CARD_JAPAN.hashCode(), 1, 1,
                Instant.ofEpochMilli(1233), false);
        cardData.put(CARD_JAPAN.hashCode(), cardSrsDataJapan);
        SrsCardsManager currentManager = new SrsCardsManager(new LessonBuilder(
                new Lesson("Capitals", List.of("Country", "Capital"), List.of("Hint"), 0,
                        1, List.of(CARD_JAPAN, CARD_BELGIUM))).build(),
                        cardData);
        List<SrsCard> srsCards = currentManager.sort();
        List<SrsCard> expected = new ArrayList<>();
        expected.add(new SrsCardBuilder().build());
        expected.add(new SrsCard(CARD_JAPAN, cardSrsDataJapan, lesson));
        assertEquals(expected, srsCards);
    }
    @Test
    public void checkUpdate() {
        Instant currentDate = Instant.now();
        quizInformation.add(List.of(0, 1, 1, 0));
        quizInformation.add(List.of(1, 1, 1, 0));
        quizInformation.add(List.of(2, 1, 1, 0));
        quizInformation.add(List.of(3, 1, 1, 0));
        quizInformation.add(List.of(4, 1, 0, 0));
        quizInformation.add(List.of(5, 1, 1, 0));
        SrsCardsManager currentManager = new SrsCardsManager(srsCards, quizInformation, currentDate);
        List<CardSrsData> cardData = currentManager
                .updateCardData();
        List<CardSrsData> expected = new ArrayList<>();
        expected.add(new CardSrsData(CARD_BELGIUM.hashCode(), 2, 2, currentDate
                .plus(Duration.ofHours(5)), false));
        expected.add(new CardSrsData(CARD_JAPAN.hashCode(), 2, 2, currentDate
                .plus(Duration.ofHours(48)), false));
        expected.add(new CardSrsData(CARD_CAT.hashCode(), 2, 2, currentDate
                .plus(Duration.ofHours(48)), false));
        expected.add(new CardSrsData(CARD_DOG.hashCode(), 2, 2, currentDate
                .plus(Duration.ofHours(24)), false));
        expected.add(new CardSrsData(CARD_DOGCAT.hashCode(), 2, 0, currentDate
                .plus(Duration.ofHours(1)), false));
        expected.add(new CardSrsData(CARD_MULTI.hashCode(), 2, 2, currentDate
                .plus(Duration.ofHours(12)), false));
        for (int i = 0; i < 5; i++) {
            assertEquals(expected.get(i).getSrsDueDate(), cardData.get(i).getSrsDueDate());
            assertEquals(expected.get(i).getNumOfAttempts(), cardData.get(i).getNumOfAttempts());
            assertEquals(expected.get(i).getStreak(), cardData.get(i).getStreak());
        }
    }
}
