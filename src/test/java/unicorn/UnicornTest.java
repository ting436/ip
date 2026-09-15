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
    public void getWelcomeMessage_returnsPrismaIntroduction() {
        assertEquals("Hello! I'm Prisma, your wise tech unicorn. "
                        + "Tell me what is on your quest list, and we'll make some magic.",
                unicorn.getWelcomeMessage());
    }

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
        assertTrue(unicorn.getResponse("delete 1").contains("quest log now holds 0 quests"));
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
        assertTrue(unicorn.getResponse("mark abc").contains("valid quest number"));
        assertTrue(unicorn.getResponse("delete 1").contains("not appeared"));
        assertTrue(unicorn.getResponse("deadline report /by tomorrow").contains("Please use"));
        assertTrue(unicorn.getResponse("unknown").contains("signal was unclear"));
    }

    @Test
    public void getResponse_invalidCommand_responseIsClassifiedAndMarkedAsError() {
        String response = unicorn.getResponse("teleport home");

        assertTrue(response.startsWith("⚠ "));
        assertEquals("error", unicorn.getCommandType());

        unicorn.getResponse("deadline report /by tomorrow");
        assertEquals("error", unicorn.getCommandType());

        unicorn.getResponse("todo read book");
        assertEquals("todo", unicorn.getCommandType());
    }

    @Test
    public void getResponse_saveFails_changeIsRolledBack() {
        Unicorn failingUnicorn = new Unicorn(tasks, ignoredTasks -> {
            throw new IOException("Test save failure");
        });

        String response = failingUnicorn.getResponse("todo read book");

        assertTrue(response.contains("could not save your quests"));
        assertEquals(0, tasks.size());
    }

    @Test
    public void getResponse_supportedCommands_usePrismaPersonality() {
        assertTrue(unicorn.getResponse("list").contains("quest log is clear"));
        assertTrue(unicorn.getResponse("todo read book").contains("added to the rainbow"));
        assertTrue(unicorn.getResponse("mark 1").contains("quest conquered"));
        assertTrue(unicorn.getResponse("unmark 1").contains("Quest reopened"));
        assertTrue(unicorn.getResponse("find book").contains("unicorn senses"));
        assertTrue(unicorn.getResponse("bye").contains("Prisma"));
    }
}
