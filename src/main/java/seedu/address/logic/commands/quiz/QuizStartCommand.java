package seedu.address.logic.commands.quiz;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.Syntax.PREFIX_START_COUNT;
import static seedu.address.logic.parser.Syntax.PREFIX_START_MODE;
import static seedu.address.logic.parser.Syntax.PREFIX_START_NAME;

import java.util.HashMap;
import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

import seedu.address.logic.commands.management.ManagementCommand;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.LessonList;
import seedu.address.model.modelmanager.ManagementModel;
import seedu.address.model.modelmanager.Model;

import seedu.address.model.modelmanager.QuizModel;

import seedu.address.model.quiz.Quiz;
import seedu.address.model.quiz.QuizCard;
import seedu.address.model.quiz.QuizMode;
import seedu.address.model.quiz.QuizUiDisplayFormatter;
import seedu.address.model.session.Session;
import seedu.address.model.session.SrsCardsManager;
import seedu.address.model.srscard.SrsCard;
import seedu.address.model.user.CardSrsData;

/**
 * Execute user input to start a session.
 */
public class QuizStartCommand extends ManagementCommand {
    public static final String COMMAND_WORD = "start";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + "Parameters: "
            + PREFIX_START_NAME + "NAME "
            + "[" + PREFIX_START_COUNT + "COUNT] "
            + PREFIX_START_MODE + "MODE...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_START_NAME + "country-capitals "
            + PREFIX_START_COUNT + "15 "
            + PREFIX_START_MODE + "LEARN";
    public static final String MESSAGE_SUCCESS = "Starting new quiz";
    private Session session;

    public QuizStartCommand(Session session) {
        requireNonNull(session);
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    /**
     * Executes the command.
     */
    public CommandResult executeActual(QuizModel model, CommandHistory history) {
        List<QuizCard> quizCards = session.generateSession();
        Quiz quiz = new Quiz(quizCards, session.getMode());
        model.init(quiz, session);
        QuizCard card = model.getNextCard();
        if (card.getQuizMode() == QuizMode.PREVIEW) {
            model.setDisplayFormatter(new QuizUiDisplayFormatter(
                    model.getQuestionHeader(), card.getQuestion(),
                    model.getAnswerHeader(), card.getAnswer(), QuizMode.PREVIEW));
        } else {
            model.setDisplayFormatter(new QuizUiDisplayFormatter(
                    model.getQuestionHeader(), card.getQuestion(),
                    model.getAnswerHeader(), QuizMode.REVIEW));
        }

        return new CommandResult("", true, false, false);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @param history {@code CommandHistory} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        // CommandException will be thrown if and only if LogicManager passes in the incorrect Model
        // In other words, only incorrect code will result in a CommandException being thrown
        if (!(model instanceof ManagementModel)) {
            throw new CommandException(MESSAGE_EXPECTED_MODEL);
        }
        ManagementModel mgtModel = (ManagementModel) model;
        LessonList lessonList = mgtModel.getLessonList();
        List<Lesson> lessons = lessonList.getLessons();
        boolean lessonExist = false;
        Lesson lesson = mgtModel.getLesson(0);
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getName().equals(this.session.getName())) {
                lesson = mgtModel.getLesson(i);
                lessonExist = true;
                break;
            }
        }
        if (!lessonExist) {
            throw new CommandException("Lesson is not found. Please try another one.");
        }
        HashMap<Integer, CardSrsData> cardData = new HashMap<>();
        for (int i = 0; i < lesson.getCardCount(); i++) {
            int currentHashcode = lesson.getCards().get(i).hashCode();
            if (mgtModel.getCardSrsData(currentHashcode) != null) {
                cardData.put(currentHashcode, mgtModel.getCardSrsData(currentHashcode));
            }
        }
        SrsCardsManager generateManager = new SrsCardsManager(lesson, cardData);
        if (this.session.getMode() == QuizMode.PREVIEW) {
            this.session = new Session(this.session.getName(), this.session.getCount(), this.session.getMode(),
                    generateManager.preview());
        } else if (this.session.getMode() == QuizMode.DIFFICULT) {
            List<SrsCard> srsCards = generateManager.previewDifficult();
            if (srsCards.size() == 0) {
                throw new CommandException("There is no difficult card in this lesson");
            } else {
                this.session = new Session(this.session.getName(), this.session.getCount(), this.session.getMode(),
                        srsCards);
            }
        } else if (this.session.getMode() == QuizMode.REVIEW) {
            List<SrsCard> srsCards = generateManager.sort();
            if (srsCards.size() == 0) {
                throw new CommandException("There is no card for review since all cards in current lesson"
                        + "do not reach the due date.");
            } else {
                this.session = new Session(this.session.getName(), this.session.getCount(), this.session.getMode(),
                        srsCards);
            }
        } else if (this.session.getMode() == QuizMode.LEARN) {
            List<SrsCard> srsCards = generateManager.learn();
            if (srsCards.size() == 0) {
                throw new CommandException("There is no more new card to learn in this lesson");
            } else {
                this.session = new Session(this.session.getName(), this.session.getCount(), this.session.getMode(),
                        srsCards);
            }
        }
        if (session.getCount() > session.getSrsCards().size()) {
            session.setCount(session.getSrsCards().size());
            return new CommandResult("Not enough cards in current lesson. Set the count to the maximum"
                    + "number for you by default.");
        } else {
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }
}
