package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.quiz.QuizMode;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }
    /**
     *
     * Parses a {@code String name} into a {@code String name}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static String parseName(String name) {
        requireNonNull(name);
        String trimmedName = name.trim();
        return trimmedName;
    }
    /**
     * Parses a {@code String count} into a {@code int count}.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the count is not a number.
     */
    public static int parseCount(String count) throws ParseException {
        requireNonNull(count);
        String trimmedCount = count.trim();
        boolean numeric = trimmedCount.matches("\\d+");
        if (numeric) {
            return Integer.parseInt(trimmedCount);
        } else {
            throw new ParseException("Count of number should an integer.");
        }
    }
    /**
     * Parses a {@code String mode} into an {@code Quiz.mode mode}.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the mode name is not in the enum.
     */
    public static QuizMode parseMode(String mode) throws ParseException {
        requireNonNull(mode);
        String trimmedMode = mode.trim().toUpperCase();
        if (trimmedMode.equals("LEARN") || trimmedMode.equals("PREVIEW")
                || trimmedMode.equals("REVIEW") || trimmedMode.equals("DIFFICULT")) {
            return QuizMode.valueOf(trimmedMode);
        } else {
            throw new ParseException("Mode of quiz is not acceptable. You can choose from: "
                    + "learn, preview, review and difficult");
        }
    }
}
