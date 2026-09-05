package unicorn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import unicorn.task.TaskList;

public class UnicornTest {
    private final TaskList tasks = new TaskList();
    private final Unicorn unicorn = new Unicorn(tasks, ignoredTasks -> { });

    @Test
    public void getResponse_addAndListTasks_tasksAreStoredAndDisplayed() {
        assertTrue(unicorn.getResponse("todo read book").contains("[T] [ ] read book"));
        assertTrue(unicorn.getResponse("deadline submit report /by 2026-09-10").contains("[D] [ ] submit report"));

        String response = unicorn.getResponse("list");

        assertTrue(response.contains("1. [T] [ ] read book"));
        assertTrue(response.contains("2. [D] [ ] submit report"));
    }

    @Test
    public void getResponse_markUnmarkAndDelete_taskIsUpdated() {
        unicorn.getResponse("todo read book");

        assertTrue(unicorn.getResponse("mark 1").contains("[T] [X] read book"));
        assertTrue(unicorn.getResponse("unmark 1").contains("[T] [ ] read book"));
        assertTrue(unicorn.getResponse("delete 1").contains("Now you have 0 tasks"));
        assertEquals(0, tasks.size());
    }

    @Test
    public void getResponse_findTask_onlyMatchingTasksAreDisplayed() {
        unicorn.getResponse("todo read book");
        unicorn.getResponse("todo buy groceries");

        String response = unicorn.getResponse("find book");

        assertTrue(response.contains("read book"));
        assertFalse(response.contains("buy groceries"));
    }

    @Test
    public void getResponse_invalidCommands_helpfulErrorsAreDisplayed() {
        assertTrue(unicorn.getResponse("mark abc").contains("valid task number"));
        assertTrue(unicorn.getResponse("delete 1").contains("does not exist"));
        assertTrue(unicorn.getResponse("deadline report /by tomorrow").contains("Please use"));
        assertTrue(unicorn.getResponse("unknown").contains("I don't understand"));
    }

    @Test
    public void getResponse_saveFails_changeIsRolledBack() {
        Unicorn failingUnicorn = new Unicorn(tasks, ignoredTasks -> {
            throw new IOException("Test save failure");
        });

        String response = failingUnicorn.getResponse("todo read book");

        assertTrue(response.contains("could not save"));
        assertEquals(0, tasks.size());
    }
}
