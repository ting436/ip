package unicorn.task;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Checks date parsing and display formatting for deadline tasks.
 */
public class DeadlineTaskTest {
    /**
     * Verifies supported deadline input formats and display formatting.
     */
    @Test
    void deadlineParsingAndDisplayWork() {
        LocalDateTime expectedDateTime = LocalDateTime.of(2019, 12, 2, 18, 0);
        LocalDateTime parsedDateTime = DeadlineTask.parseBy("2/12/2019 1800");
        assertEquals(expectedDateTime, parsedDateTime, "The example date and time should be parsed.");

        DeadlineTask task = new DeadlineTask("return book", parsedDateTime);
        assertEquals("[D] [ ] return book (by: Dec 02 2019 6:00 PM)", task.toString(),
                "A deadline should use the display format.");

        LocalDateTime expectedDate = LocalDateTime.of(2019, 10, 15, 0, 0);
        assertEquals(expectedDate, DeadlineTask.parseBy("2019-10-15"),
                "A date-only deadline should start at midnight.");
    }
}
