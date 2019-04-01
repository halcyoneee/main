package seedu.address.model.modelmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.UserPrefs;
import seedu.address.model.card.Card;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.LessonList;
import seedu.address.model.user.CardSrsData;
import seedu.address.model.user.User;
import seedu.address.testutil.TypicalCards;
import seedu.address.testutil.TypicalLessonList;

public class ManagementModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ManagementModelManager modelManager = new ManagementModelManager();

    private Lesson getTestLesson() {
        ArrayList<String> testFields = new ArrayList<>();
        testFields.add("test 1");
        testFields.add("test 2");

        return new Lesson("test", 2, testFields);
    }

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new LessonList().getLessons(), modelManager.getLessons());
        assertEquals(new User(), modelManager.getUser());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setUserPrefs(null);
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setGuiSettings(null);
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void lessonsTests() {
        /*
         * Note: This is an integration test that verifies the ManagementModel is properly wired to the
         * {@link LessonList} class.
         * More extensive testing of LessonList functionality is done in {@link LessonList} class.
         */
        Lesson testLesson = getTestLesson();
        assertEquals(0, modelManager.getLessons().size());
        modelManager.addLesson(getTestLesson());
        assertEquals(testLesson, modelManager.getLesson(0));
        testLesson.setName("other name");
        modelManager.setLesson(0, testLesson);
        assertEquals(testLesson, modelManager.getLesson(0));
        modelManager.deleteLesson(0);
        assertEquals(0, modelManager.getLessons().size());
    }

    @Test
    public void userTests() {
        /*
         * Note: This is an integration test that verifies the ManagementModel is properly wired to the
         * {@link User} class.
         * More extensive testing of User functionality is done in {@link User} class.
         *
         * TODO
         */
        CardSrsData testCardSrsData = new CardSrsData(1, 1, 1,
                Instant.now(), false);

        User testUser = modelManager.getUser();
        assertEquals(new User(), testUser);
        modelManager.addCardSrsData(testCardSrsData);
        assertEquals(testCardSrsData, modelManager.getCardSrsData(testCardSrsData.getHashCode()));
        modelManager.setCardSrsData(testCardSrsData);
        assertEquals(testCardSrsData, modelManager.getCardSrsData(testCardSrsData.getHashCode()));
        modelManager.deleteCardSrsData(testCardSrsData);
        assertEquals(0, testUser.getCards().size());

    }

    @Test
    public void equals() {
        UserPrefs userPrefs = new UserPrefs();
        LessonList lessonList = new LessonList();
        User user = new User();

        // same values -> returns true
        modelManager = new ManagementModelManager(userPrefs, lessonList, user);
        ManagementModelManager modelManagerCopy = new ManagementModelManager(userPrefs, lessonList, user);
        assertEquals(modelManager, modelManagerCopy);

        // same object -> returns true
        assertEquals(modelManager, modelManager);

        // null -> returns false
        assertNotNull(modelManager);

        // different types -> returns false
        assertNotEquals(modelManager, 5);
    }

    @Test
    public void getLessonList() {
        // by default, lessonList is empty
        assertEquals(modelManager.getLessonList(), new LessonList());
    }

    @Test
    public void setLessonList() {
        LessonList test = new LessonList();
        test.addLesson(getTestLesson());
        assertEquals(modelManager.getLessonList(), new LessonList());
        modelManager.setLessonList(test);
        assertEquals(test, modelManager.getLessonList());
    }
    @Test
    public void openLesson() {
        Lesson lesson = TypicalLessonList.LESSON_DEFAULT;
        modelManager.addLesson(lesson);
        modelManager.openLesson(0); // Open added lesson
        // Adding lesson to modelManager should not change the lesson
        assertEquals(modelManager.getOpenedLesson(), lesson);
        assertEquals(modelManager.getOpenedLessonCards(), lesson.getCards());
        assertEquals(modelManager.getOpenedLessonCoreHeaders(), lesson.getCoreHeaders());
        assertEquals(modelManager.getOpenedLessonOptionalHeaders(), lesson.getOptionalHeaders());
    }

    @Test
    public void closeLesson() {
        Lesson lesson = TypicalLessonList.LESSON_DEFAULT;
        modelManager.addLesson(lesson);
        modelManager.openLesson(0); // Open added lesson
        assertEquals(modelManager.getOpenedLesson(), lesson);
        modelManager.closeLesson();
        assertEquals(modelManager.getOpenedLesson(), null);
    }

    @Test
    public void addCardToOpenedLesson() {
        Lesson lesson = TypicalLessonList.LESSON_DEFAULT;
        Card card = TypicalCards.CARD_CAT;

        modelManager.addLesson(lesson);
        modelManager.openLesson(0); // Open added lesson
        int size = modelManager.getOpenedLesson().getCardCount();
        modelManager.addCardToOpenedLesson(card);
        assertEquals(size + 1, modelManager.getOpenedLesson().getCardCount());
    }

    @Test
    public void deleteCardFromOpenedLesson() {
        Lesson lesson = TypicalLessonList.LESSON_DEFAULT;

        modelManager.addLesson(lesson);
        modelManager.openLesson(0); // Open added lesson
        int size = modelManager.getOpenedLesson().getCardCount();
        modelManager.deleteCardFromOpenedLesson(0);
        assertEquals(size - 1, modelManager.getOpenedLesson().getCardCount());
    }
}
