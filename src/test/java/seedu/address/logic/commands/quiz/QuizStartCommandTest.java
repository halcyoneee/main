package seedu.address.logic.commands.quiz;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalCards.CARD_JAPAN;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.modelmanager.QuizModel;
import seedu.address.model.modelmanager.QuizModelManager;
import seedu.address.model.quiz.Quiz;
import seedu.address.model.quiz.QuizCard;
import seedu.address.model.quiz.QuizMode;
import seedu.address.model.session.Session;
import seedu.address.model.srscard.SrsCard;
import seedu.address.model.user.CardSrsData;
import seedu.address.testutil.Assert;
import seedu.address.testutil.LessonBuilder;
import seedu.address.testutil.SessionBuilder;
import seedu.address.testutil.SrsCardBuilder;


public class QuizStartCommandTest {
    private static final CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_throwsNullPointerException () {
        Assert.assertThrows(NullPointerException.class, () ->
                new QuizStartCommand(null));
    }
    //TODO: fix it.
    /*@Test
    public void execute_success() throws Exception {
        ManagementModel managementModel = new ManagementModelManager();
        Session session = new SessionBuilder().build_without_srsCards();
        CommandResult commandResult = new QuizStartCommand(session).execute(managementModel, commandHistory);
        assertEquals(QuizStartCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
    }*/

    @Test
    public void executeActual_learn_success() {
        Lesson lesson = new LessonBuilder().build();
        final Session session = new SessionBuilder(new Session("Capitals", 2,
                QuizMode.LEARN, List.of(new SrsCardBuilder().build(),
                new SrsCardBuilder(new SrsCard(CARD_JAPAN, new CardSrsData(CARD_JAPAN.hashCode(), 1,
                        1, Instant.now().plus(Duration.ofHours(2)), false), lesson)).build()))).build();
        final QuizCard card1 = new QuizCard("Belgium", "Brussels");
        final QuizCard card2 = new QuizCard("Japan", "Tokyo");
        final List<QuizCard> quizCards = new ArrayList<>(Arrays.asList(card1, card2));
        final Quiz quiz = new Quiz(quizCards, QuizMode.LEARN);

        QuizModelManager expectedModel = new QuizModelManager();
        expectedModel.init(quiz, session);
        expectedModel.getNextCard();
        CommandResult expectedCommandResult = new QuizStartCommand(session).executeActual(expectedModel,
            commandHistory);

        QuizModel actualModel = new QuizModelManager();
        QuizStartCommand quizStartCommand = new QuizStartCommand(session);
        assertEquals(quizStartCommand.getSession(), session);

        CommandHistory expectedCommandHistory = new CommandHistory(commandHistory);
        CommandResult result = quizStartCommand.executeActual(actualModel, commandHistory);
        assertEquals(expectedCommandResult, result);
        assertEquals(expectedCommandHistory, commandHistory);

    }

    @Test
    public void executeActual_review_success() {
        Lesson lesson = new LessonBuilder().build();
        final Session session = new SessionBuilder(new Session("Capitals", 2,
            QuizMode.REVIEW, List.of(new SrsCardBuilder().build(),
            new SrsCardBuilder(new SrsCard(CARD_JAPAN, new CardSrsData(CARD_JAPAN.hashCode(), 1,
                1, Instant.now().plus(Duration.ofHours(2)), false), lesson)).build()))).build();
        final QuizCard card1 = new QuizCard("Belgium", "Brussels");
        final QuizCard card2 = new QuizCard("Japan", "Tokyo");
        final List<QuizCard> quizCards = new ArrayList<>(Arrays.asList(card1, card2));
        final Quiz quiz = new Quiz(quizCards, QuizMode.REVIEW);

        QuizModelManager expectedModel = new QuizModelManager();
        expectedModel.init(quiz, session);
        expectedModel.getNextCard();
        CommandResult expectedCommandResult = new QuizStartCommand(session).executeActual(expectedModel,
            commandHistory);

        QuizModel actualModel = new QuizModelManager();
        QuizStartCommand quizStartCommand = new QuizStartCommand(session);
        assertEquals(quizStartCommand.getSession(), session);

        CommandHistory expectedCommandHistory = new CommandHistory(commandHistory);
        CommandResult result = quizStartCommand.executeActual(actualModel, commandHistory);
        assertEquals(expectedCommandResult, result);
        assertEquals(expectedCommandHistory, commandHistory);
    }
}
