package seedu.address.logic.commands.quiz;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.modelmanager.Model;
import seedu.address.model.modelmanager.QuizModel;
import seedu.address.model.quiz.QuizCard;
import seedu.address.model.quiz.QuizMode;
import seedu.address.model.quiz.QuizUiDisplayFormatter;

/**
 * Processes user answer
 */
public class QuizAnswerCommand extends QuizCommand {
    public static final String COMMAND_WORD = "answer";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": * any character except word that starts with \\\n";
    public static final String MESSAGE_CORRECT = "Your answer is correct.\n";
    public static final String MESSAGE_WRONG_ONCE = "Your answer %1$s is wrong, "
            + "you have one more chance to answer it.\n";
    public static final String MESSAGE_WRONG = "Your answer is %1$s but the correct answer is %2$s.\n";
    public static final String MESSAGE_SUCCESS = "You have completed all the questions in this quiz.\n";

    private String answer;
    public QuizAnswerCommand(String answer) {
        requireNonNull(answer);
        this.answer = answer;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        QuizModel quizModel = requireQuizModel(model);

        QuizCard card = quizModel.getCurrentQuizCard();
        StringBuilder sb = new StringBuilder();
        String questionHeader = quizModel.getQuestionHeader();
        String answerHeader = quizModel.getAnswerHeader();
        if (card.getQuizMode() == QuizMode.PREVIEW || card.getQuizMode() == QuizMode.DIFFICULT) {
            if (quizModel.hasCardLeft()) {
                card = quizModel.getNextCard();
                if (card.getQuizMode() == QuizMode.PREVIEW || card.getQuizMode() == QuizMode.DIFFICULT) {
                    quizModel.setDisplayFormatter(new QuizUiDisplayFormatter(questionHeader, card.getQuestion(),
                            answerHeader, card.getAnswer(), QuizMode.PREVIEW));
                    return new CommandResult("", true, false, false);
                }
                quizModel.setDisplayFormatter(new QuizUiDisplayFormatter(questionHeader, card.getQuestion(),
                        answerHeader, QuizMode.REVIEW));
                return new CommandResult("", true, false, false);
            }

            // don't need to update card of 0 attempts
            quizModel.end();

            // set the display to blank for management mode display
            quizModel.setDisplayFormatter(null);
            return new CommandResult(MESSAGE_SUCCESS, true, false, false);
        }

        boolean result = quizModel.updateTotalAttemptsAndStreak(card.getIndex(), answer);

        if (result) {
            sb.append(MESSAGE_CORRECT);

            if (quizModel.hasCardLeft()) {
                card = quizModel.getNextCard();
                quizModel.setDisplayFormatter(new QuizUiDisplayFormatter(questionHeader, card.getQuestion(),
                        answerHeader, QuizMode.REVIEW));
            } else {
                quizModel.updateUserProfile(quizModel.end());

                // set the display to blank for management mode display
                quizModel.setDisplayFormatter(null);
                sb.append(MESSAGE_SUCCESS);
            }

        } else {
            if (!card.isWrongTwice()) {
                sb.append(String.format(MESSAGE_WRONG_ONCE, answer));
                quizModel.setDisplayFormatter(new QuizUiDisplayFormatter(questionHeader, card.getQuestion(),
                        answerHeader, QuizMode.REVIEW));
            } else {
                sb.append(String.format(MESSAGE_WRONG, answer, card.getAnswer()));
                quizModel.setDisplayFormatter(new QuizUiDisplayFormatter(questionHeader, card.getQuestion(),
                        answerHeader, card.getAnswer(), QuizMode.PREVIEW));
            }
        }

        return new CommandResult(sb.toString(), true, false, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof QuizAnswerCommand // instanceof handles nulls
                && answer.equals(((QuizAnswerCommand) other).answer)); // state check
    }
}
