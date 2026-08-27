package unicorn.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Checks the completion status displayed by a task.
 */
public class TaskTest {
    /**
     * Verifies that the status icon reflects a new, completed, and reopened task.
     */
    @Test
    void getStatusIcon_taskCompletionChanges_iconMatchesCompletionState() {
        Task task = new Task("read book");

        assertEquals(" ", task.getStatusIcon(), "A new task should have an empty status icon.");

        task.markAsDone();
        assertEquals("X", task.getStatusIcon(), "A completed task should have an X status icon.");

        task.markAsUndone();
        assertEquals(" ", task.getStatusIcon(), "A reopened task should have an empty status icon.");
    }
}
