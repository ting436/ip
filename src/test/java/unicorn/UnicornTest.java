package unicorn;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unicorn.task.Task;
import unicorn.task.TaskList;
import unicorn.task.TodoTask;
import unicorn.ui.Ui;

/**
 * Checks command-loop routing for the Unicorn chatbot.
 */
public class UnicornTest {
    /**
     * Verifies that the loop routes commands in order and stops at {@code bye}.
     */
    @Test
    void runCommandLoop_listAndUnknownCommands_commandsRoutedUntilBye() {
        RecordingUi ui = new RecordingUi("list", "unknown", "bye");
        TaskList tasks = new TaskList(List.of(new TodoTask("read book")));

        Unicorn.runCommandLoop(tasks, ui);

        assertEquals(1, ui.taskListDisplayCount, "The list command should display the task list once.");
        assertEquals(1, ui.helpDisplayCount, "An unknown command should display help once.");
        assertEquals(0, ui.remainingCommandCount(), "The command loop should consume input through bye.");
    }

    /**
     * Records selected UI calls without writing to the console.
     */
    private static class RecordingUi extends Ui {
        private final Queue<String> commands;
        private int taskListDisplayCount;
        private int helpDisplayCount;

        RecordingUi(String... commands) {
            this.commands = new ArrayDeque<>(List.of(commands));
        }

        @Override
        public String readCommand() {
            return commands.remove();
        }

        @Override
        public void showTaskList(TaskList tasks) {
            taskListDisplayCount++;
        }

        @Override
        public void showHelp() {
            helpDisplayCount++;
        }

        int remainingCommandCount() {
            return commands.size();
        }

        @Override
        public void showMatchingTasks(List<Task> matchingTasks) {
            throw new AssertionError("The test should not display matching tasks.");
        }

        @Override
        public void showError(String message) {
            throw new AssertionError("The test should not display an error.");
        }
    }
}
