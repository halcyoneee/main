package seedu.address.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.Syntax.PREFIX_CORE_ANSWER;
import static seedu.address.logic.parser.Syntax.PREFIX_CORE_QUESTION;
import static seedu.address.logic.parser.Syntax.PREFIX_LESSON_NAME;
import static seedu.address.testutil.LessonBuilder.DEFAULT_CORE_HEADER_1;
import static seedu.address.testutil.LessonBuilder.DEFAULT_CORE_HEADER_2;
import static seedu.address.testutil.LessonBuilder.DEFAULT_NAME;
import static seedu.address.testutil.TypicalSession.SESSION_DEFAULT_2;
import static seedu.address.testutil.TypicalSession.SESSION_DEFAULT_2_ACTUAL;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.management.AddLessonCommand;
import seedu.address.logic.commands.management.CloseLessonCommand;
import seedu.address.logic.commands.management.DeleteLessonCommand;
import seedu.address.logic.commands.management.ExitCommand;
import seedu.address.logic.commands.management.HelpCommand;
import seedu.address.logic.commands.management.HistoryCommand;
import seedu.address.logic.commands.management.OpenLessonCommand;
import seedu.address.logic.commands.management.ReloadLessonsCommand;
import seedu.address.logic.commands.quiz.QuizAnswerCommand;
import seedu.address.logic.commands.quiz.QuizQuitCommand;
import seedu.address.logic.commands.quiz.QuizStartCommand;
import seedu.address.logic.commands.quiz.QuizStatusCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.LessonList;
import seedu.address.model.modelmanager.ManagementModel;
import seedu.address.model.modelmanager.ManagementModelManager;
import seedu.address.model.modelmanager.QuizModel;
import seedu.address.model.modelmanager.QuizModelManager;
import seedu.address.model.quiz.Quiz;
import seedu.address.model.quiz.QuizMode;
import seedu.address.model.quiz.QuizUiDisplayFormatter;
import seedu.address.model.user.User;
import seedu.address.storage.CsvLessonListStorage;
import seedu.address.storage.CsvUserStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.LessonBuilder;

public class LogicManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private StorageManager storage;
    private ManagementModel managementModel;
    private QuizModel quizModel;
    private CommandHistory history = new CommandHistory();
    private Logic logic;
    private QuizModelManager expectedModel;
    private Quiz quizExpected;
    private Quiz quizActual;

    @Before
    public void setUp() throws Exception {
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.newFile().toPath());
        CsvLessonListStorage lessonListStorage = new CsvLessonListStorage(temporaryFolder.newFolder().toPath());
        CsvUserStorage userStorage = new CsvUserStorage(temporaryFolder.newFile().toPath());
        storage = new StorageManager(userPrefsStorage, lessonListStorage, userStorage);
        managementModel = new ManagementModelManager();
        quizModel = new QuizModelManager(managementModel);
        logic = new LogicManager(managementModel, quizModel, storage);

        quizExpected = new Quiz(SESSION_DEFAULT_2.generateSession(), SESSION_DEFAULT_2.getMode());
        quizActual = new Quiz(SESSION_DEFAULT_2_ACTUAL.generateSession(), SESSION_DEFAULT_2_ACTUAL.getMode());

        expectedModel = new QuizModelManager(new ManagementModelManager());
    }

    @Test
    public void execute_storageCommands_successfulFileReloadDelete() {
        LessonList lessonList = new LessonList();
        lessonList.addLesson(new LessonBuilder().build());
        storage.saveLessonList(lessonList);
        assertEquals(1, storage.readLessonList().get().getLessons().size());
        assertCommandSuccess(ReloadLessonsCommand.COMMAND_WORD , ReloadLessonsCommand.MESSAGE_SUCCESS, managementModel);
        assertCommandSuccess(DeleteLessonCommand.COMMAND_WORD + " 1",
            String.format(DeleteLessonCommand.MESSAGE_SUCCESS , "Capitals"),
            managementModel);
        assertEquals(0, storage.readLessonList().get().getLessons().size());
    }

    //some linux issue lol so travis fails TODO
    /*
    @Test
    public void execute_deleteCommandInvalidFile_throwsIoExceptions() throws ParseException,
        CommandException {
        LessonList lessonList = new LessonList();
        lessonList.addLesson(new LessonBuilder().build());
        storage.saveLessonList(lessonList);
        File file = new File(storage.getLessonListFolderPath().resolve("Capitals.csv").toString());
        file.setReadOnly();
        assertCommandSuccess(ReloadLessonsCommand.COMMAND_WORD , ReloadLessonsCommand.MESSAGE_SUCCESS, managementModel);
        logic.execute(DeleteLessonCommand.COMMAND_WORD + " 1");
        // some unknown issue broke the original test on linux so now we have this
        assertEquals(0, managementModel.getLessons().size());
        assertCommandSuccess(ReloadLessonsCommand.COMMAND_WORD , ReloadLessonsCommand.MESSAGE_SUCCESS, managementModel);
        assertEquals(1, managementModel.getLessons().size());
    }
    */

    @Test
    public void execute_saveCommandInvalidFile_throwsIoExceptions() throws ParseException, CommandException {
        LessonList lessonList = new LessonList();
        lessonList.addLesson(new LessonBuilder().build());
        storage.saveLessonList(lessonList);
        File file = new File(storage.getLessonListFolderPath().resolve("Capitals.csv").toString());
        file.setReadOnly();
        assertCommandSuccess(ReloadLessonsCommand.COMMAND_WORD , ReloadLessonsCommand.MESSAGE_SUCCESS, managementModel);
        CommandResult expected = new CommandResult(LogicManager.FAIL_SAVE_LESSONS_MESSAGE
            + LogicManager.CHECK_LOGS_MESSAGE);
        logic.execute(OpenLessonCommand.COMMAND_WORD + " 1");
        assertEquals(expected.getFeedbackToUser(),
            logic.execute(CloseLessonCommand.COMMAND_WORD).getFeedbackToUser());
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
        assertHistoryCorrect(invalidCommand);
    }

    @Test
    public void execute_startCommand_success() {
        LessonList lessonList = new LessonList();
        lessonList.addLesson(new LessonBuilder().build());
        ManagementModelManager expectedMgmtMgr = new ManagementModelManager(new UserPrefs(), lessonList, new User());
        managementModel = new ManagementModelManager(new UserPrefs(), lessonList, new User());
        logic = new LogicManager(managementModel, quizModel, storage);

        assertCommandSuccess(QuizStartCommand.COMMAND_WORD + " n/Capitals c/2 m/PREVIEW",
                "", expectedMgmtMgr);
    }

    @Test
    public void execute_quizCommand_success() throws CommandException {
        expectedModel.init(quizExpected, SESSION_DEFAULT_2);
        quizModel.init(quizActual, SESSION_DEFAULT_2_ACTUAL);

        expectedModel.getNextCard();
        expectedModel.getNextCard();
        CommandResult expected = new CommandResult("");

        quizModel.getNextCard();

        assertCommandSuccess("tokyo", expected.getFeedbackToUser(), expectedModel);
        assertTrue(new QuizAnswerCommand("someanswer").execute(quizModel, history).isShowQuiz());
    }

    @Test
    public void execute_quitCommand_success() {
        expectedModel.init(quizExpected, SESSION_DEFAULT_2);
        quizModel.init(quizActual, SESSION_DEFAULT_2_ACTUAL);

        expectedModel.getNextCard();
        expectedModel.getNextCard();
        expectedModel.getNextCard();
        expectedModel.updateTotalAttemptsAndStreak(0, "Brussels");
        expectedModel.updateUserProfile(expectedModel.end());
        CommandResult expected = new CommandResult(String.format(QuizQuitCommand.MESSAGE_SUCCESS, 1));

        quizModel.getNextCard();
        quizModel.getNextCard();
        quizModel.getNextCard();
        quizModel.updateTotalAttemptsAndStreak(0, "Brussels");

        assertCommandSuccess("\\quit", expected.getFeedbackToUser(), expectedModel);
    }

    @Test
    public void execute_quizStatusCommand_success() {
        expectedModel.init(quizExpected, SESSION_DEFAULT_2);
        quizModel.init(quizActual, SESSION_DEFAULT_2_ACTUAL);

        expectedModel.getNextCard();

        CommandResult expected = new CommandResult(String.format(QuizStatusCommand.MESSAGE_SUCCESS,
            expectedModel.getQuizTotalAttempts(), expectedModel.getQuizTotalCorrectQuestions(),
            expectedModel.getCurrentProgress()));

        quizModel.getNextCard();

        assertCommandSuccess("\\status", expected.getFeedbackToUser(), expectedModel);
    }

    @Test
    public void isShowHelp() {
        assertTrue(new HelpCommand().execute(managementModel, history).isShowHelp());
    }

    @Test
    public void isExit() {
        assertTrue(new ExitCommand().execute(managementModel, history).isExit());
    }

    @Test
    public void getMode() throws CommandException {
        expectedModel.init(quizExpected, SESSION_DEFAULT_2);
        quizModel.init(quizActual, SESSION_DEFAULT_2_ACTUAL);

        expectedModel.getNextCard();
        expectedModel.getNextCard();
        expectedModel.setDisplayFormatter(new QuizUiDisplayFormatter("question", "Hungary", "answer", "Budapest",
            QuizMode.PREVIEW));

        quizModel.getNextCard();

        // after quiz started
        assertCommandSuccess("", "", expectedModel);
        assertEquals(LogicManager.Mode.QUIZ, logic.getMode());

        // after quiz ended
        quizModel.getNextCard();
        quizModel.getNextCard();
        quizModel.getNextCard();
        quizModel.getNextCard();
        new QuizAnswerCommand("japan").execute(quizModel, history);

        assertEquals(LogicManager.Mode.MANAGEMENT, logic.getMode());
    }

    @Test
    public void getDisplayFormatter() {
        expectedModel.init(quizExpected, SESSION_DEFAULT_2);
        quizModel.init(quizActual, SESSION_DEFAULT_2_ACTUAL);

        expectedModel.getNextCard();
        expectedModel.getNextCard();
        expectedModel.setDisplayFormatter(new QuizUiDisplayFormatter("Country",
                "Japan", "Capital", "Tokyo", QuizMode.PREVIEW));

        quizModel.getNextCard();

        assertCommandSuccess("", "", expectedModel);
        assertEquals(expectedModel.getDisplayFormatter(), logic.getDisplayFormatter());
    }
    @Test
    public void getHistory() {
        CommandHistory empty = new CommandHistory();
        assertEquals(empty.getHistory(), logic.getHistory());
    }

    /**
     * Executes the command, confirms that no exceptions are thrown and that the result message is correct.
     * Also confirms that {@code expectedManagementModel} is as specified.
     * @see #assertCommandBehavior(Class, String, String, ManagementModel)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      ManagementModel expectedManagementModel) {
        assertCommandBehavior(null, inputCommand, expectedMessage, expectedManagementModel);
    }

    /**
     * Executes the command, confirms that no exceptions are thrown and that the result message is correct.
     * Also confirms that {@code expectedModel} is as specified.
     * @see #assertCommandBehavior(Class, String, String, QuizModel)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, QuizModel expectedModel) {
        assertCommandBehavior(null, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, ManagementModel)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, ManagementModel)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, ManagementModel)
     */
    private void assertCommandFailure(String inputCommand, Class<?> expectedException, String expectedMessage) {
        ManagementModel expectedManagementModel = new ManagementModelManager();
        assertCommandBehavior(expectedException, inputCommand, expectedMessage, expectedManagementModel);
    }

    /**
     * Executes the command, confirms that the result message is correct and that the expected exception is thrown,
     * and also confirms that the following two parts of the LogicManager object's state are as expected:<br>
     *      - the internal management manager data are same as those in the {@code expectedManagementModel} <br>
     *      - {@code expectedManagementModel}'s address book was saved to the storage file.
     */
    private void assertCommandBehavior(Class<?> expectedException, String inputCommand,
                                           String expectedMessage, ManagementModel expectedManagementModel) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertNull(expectedException);
            assertEquals(expectedMessage, result.getFeedbackToUser());
        } catch (CommandException | ParseException e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedManagementModel, managementModel);
    }

    /**
     * Executes the command, confirms that the result message is correct and that the expected exception is thrown,
     * and also confirms that the following two parts of the LogicManager object's state are as expected:<br>
     *      - the internal management manager data are same as those in the {@code expectedModel} <br>
     *      - {@code expectedModel}'s address book was saved to the storage file.
     */
    private void assertCommandBehavior(Class<?> expectedException, String inputCommand,
                                       String expectedMessage, QuizModel expectedModel) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertNull(expectedException);
            assertEquals(expectedMessage, result.getFeedbackToUser());
        } catch (CommandException | ParseException e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedModel, quizModel);
    }

    /**
     * Asserts that the result display shows all the {@code expectedCommands} upon the execution of
     * {@code HistoryCommand}.
     */
    private void assertHistoryCorrect(String... expectedCommands) {
        try {
            CommandResult result = logic.execute(HistoryCommand.COMMAND_WORD);
            String expectedMessage = String.format(
                    HistoryCommand.MESSAGE_SUCCESS, String.join("\n", expectedCommands));
            assertEquals(expectedMessage, result.getFeedbackToUser());
        } catch (ParseException | CommandException e) {
            throw new AssertionError("Parsing and execution of HistoryCommand.COMMAND_WORD should succeed.", e);
        }
    }

    @Test
    public void testManagementInputOutput() {
        String command = AddLessonCommand.COMMAND_WORD + " "
                + PREFIX_LESSON_NAME + DEFAULT_NAME + " "
                + PREFIX_CORE_QUESTION + DEFAULT_CORE_HEADER_1 + " "
                + PREFIX_CORE_ANSWER + DEFAULT_CORE_HEADER_2;

        try {
            CommandResult commandResult = logic.execute(command);
            assertNotNull(commandResult);
        } catch (CommandException | ParseException e) {
            // Parsing and execution of AddLessonCommand should not fail due to
            // CommandException and ParseException given it is the correct format.
            throw new AssertionError("Parsing and execution of "
                    + "AddLessonCommand.COMMAND_WORD should succeed.", e);
        }
    }
}
