package braintrain.model.quiz;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import braintrain.testutil.Assert;

public class QuizCardTest {

    private static final String QUESTION = "Japan";
    private static final String ANSWER = "Tokyo";

    private static final List<String> FIELDS_OPTIONALS = Arrays.asList("JP", "Asia");
    private static final List<String> FIELDS_OPTIONALS_EMPTY = Arrays.asList("", "");

    private static final QuizCard VALID_QUIZCARD = new QuizCard(QUESTION, ANSWER, FIELDS_OPTIONALS);
    private static final QuizCard VALID_QUIZCARD_NO_OPT = new QuizCard(QUESTION, ANSWER);


    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () ->
            new QuizCard(null, null));

        Assert.assertThrows(NullPointerException.class, () ->
            new QuizCard(QUESTION, null));

        Assert.assertThrows(NullPointerException.class, () ->
            new QuizCard(null, null, null));

        Assert.assertThrows(NullPointerException.class, () ->
            new QuizCard(QUESTION, null, FIELDS_OPTIONALS));

        Assert.assertThrows(NullPointerException.class, () ->
            new QuizCard(QUESTION, ANSWER, null));
    }

    @Test
    public void constructor_invalidQuizCard_throwsIllegalArgumentException() {
        String invalidQn = "";
        String invalidAns = "";
        Assert.assertThrows(IllegalArgumentException.class, () ->
            new QuizCard(invalidQn, invalidAns));

        Assert.assertThrows(IllegalArgumentException.class, () ->
            new QuizCard("     ", ANSWER));

        Assert.assertThrows(IllegalArgumentException.class, () ->
            new QuizCard(invalidQn, ANSWER));

        Assert.assertThrows(IllegalArgumentException.class, () ->
            new QuizCard(invalidQn, invalidAns, FIELDS_OPTIONALS_EMPTY));

        Assert.assertThrows(IllegalArgumentException.class, () ->
            new QuizCard(invalidQn, ANSWER, FIELDS_OPTIONALS_EMPTY));

        Assert.assertThrows(IllegalArgumentException.class, () ->
            new QuizCard("     ", invalidAns, FIELDS_OPTIONALS_EMPTY));

        Assert.assertThrows(IllegalArgumentException.class, () ->
            new QuizCard(QUESTION, invalidAns, FIELDS_OPTIONALS_EMPTY));

    }

    @Test
    public void getQuestion() {
        assertEquals(VALID_QUIZCARD.getQuestion(), QUESTION);
    }

    @Test
    public void getAnswer() {
        assertEquals(VALID_QUIZCARD.getAnswer(), ANSWER);
    }

    @Test
    public void getOpt() {
        assertEquals(VALID_QUIZCARD.getOpt(), FIELDS_OPTIONALS);
    }

}
