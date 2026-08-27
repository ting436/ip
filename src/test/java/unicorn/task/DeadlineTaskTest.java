package unicorn.task;

import java.time.LocalDateTime;

/**
 * Checks date parsing and display formatting for deadline tasks.
 */
public class DeadlineTaskTest {
    /**
     * Runs the deadline checks without requiring an external test library.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        LocalDateTime expectedDateTime = LocalDateTime.of(2019, 12, 2, 18, 0);
        LocalDateTime parsedDateTime = DeadlineTask.parseBy("2/12/2019 1800");
        assertEqual(expectedDateTime, parsedDateTime, "The example date and time should be parsed.");

        DeadlineTask task = new DeadlineTask("return book", parsedDateTime);
        assertEqual("[D] [ ] return book (by: Dec 02 2019 6:00 PM)", task.toString(),
                "A deadline should use the display format.");

        LocalDateTime expectedDate = LocalDateTime.of(2019, 10, 15, 0, 0);
        assertEqual(expectedDate, DeadlineTask.parseBy("2019-10-15"),
                "A date-only deadline should start at midnight.");
    }

    /**
     * Checks that two values are equal.
     *
     * @param expected expected value
     * @param actual actual value
     * @param message failure explanation
     */
    private static void assertEqual(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message);
        }
    }
}
