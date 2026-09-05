package unicorn;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import unicorn.storage.Storage;
import unicorn.task.DeadlineTask;
import unicorn.task.EventTask;
import unicorn.task.Task;
import unicorn.task.TaskList;
import unicorn.task.TodoTask;
import unicorn.ui.Ui;

/**
 * Runs the Unicorn task chatbot.
 */
public class Unicorn {
    private static final String BANNER = "          /\\\n"
            + "         /  \\\n"
            + "        / /\\ \\\n"
            + "       / /  \\ \\\n"
            + "      /_/    \\_\\\n"
            + "     (  ^  ^  )\n"
            + "      \\  ♥  /\n"
            + "       \\___/\n"
            + "      /|   |\\\n"
            + "     /_|___|_\\\n"
            + "       /   \\\n"
            + "      ✨   ✨\n";

    /**
     * Starts the chatbot, loading existing tasks and saving each successful list change.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList tasks = loadTasks(ui);

        ui.showWelcome(BANNER);
        runCommandLoop(tasks, ui);
        ui.showGoodbye();
    }

    /**
     * Reads and executes commands until the user exits the chatbot.
     *
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to read commands and display results
     */
    static void runCommandLoop(TaskList tasks, Ui ui) {
        String input = ui.readCommand();
        while (!Objects.equals(input, "bye")) {
            executeCommand(input, tasks, ui);
            input = ui.readCommand();
        }
    }

    /**
     * Routes a command to the operation that handles it.
     *
     * @param input command entered by the user
     * @param tasks tasks managed by the chatbot
     * @param ui user interface used to display results
     */
    private static void executeCommand(String input, TaskList tasks, Ui ui) {
        if (Objects.equals(input, "list")) {
            ui.showTaskList(tasks);
        } else if (input.startsWith("find ")) {
            findTasks(input.substring(5), tasks, ui);
        } else if (input.startsWith("mark ")) {
            markTask(input.substring(5), tasks, ui);
        } else if (input.startsWith("unmark ")) {
            unmarkTask(input.substring(7), tasks, ui);
        } else if (input.startsWith("delete ")) {
            deleteTask(input.substring(7), tasks, ui);
        } else {
            addTask(input, tasks, ui);
        }
    }

    /**
     * Displays tasks matching a non-blank keyword.
     */
    private static void findTasks(String keyword, TaskList tasks, Ui ui) {
        if (keyword.isBlank()) {
            ui.showError("OOPS!!! Please specify a keyword to find.");
            return;
        }
        ui.showMatchingTasks(tasks.find(keyword));
    }

    /**
     * Marks the task identified by a user-supplied number.
     */
    private static void markTask(String argument, TaskList tasks, Ui ui) {
        if (argument.isBlank()) {
            ui.showError("OOPS!!! Please specify a task number.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument);
            if (!isExistingTaskNumber(taskNumber, tasks)) {
                ui.showError("OOPS!!! That task number does not exist.");
                return;
            }

            Task task = tasks.get(taskNumber - 1);
            boolean wasDone = task.isDone();
            task.markAsDone();
            if (saveTasks(tasks, ui)) {
                ui.showTaskMarked(task);
            } else {
                restoreStatus(task, wasDone);
            }
        } catch (NumberFormatException e) {
            ui.showError("OOPS!!! Please provide a valid task number.");
        }
    }

    /**
     * Unmarks the task identified by a user-supplied number.
     */
    private static void unmarkTask(String argument, TaskList tasks, Ui ui) {
        if (argument.isBlank()) {
            ui.showError("OOPS!!! Please specify a task number.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument);
            if (!isExistingTaskNumber(taskNumber, tasks)) {
                ui.showError("OOPS!!! That task number does not exist.");
                return;
            }

            Task task = tasks.get(taskNumber - 1);
            boolean wasDone = task.isDone();
            task.markAsUndone();
            if (saveTasks(tasks, ui)) {
                ui.showTaskUnmarked(task);
            } else {
                restoreStatus(task, wasDone);
            }
        } catch (NumberFormatException e) {
            ui.showError("OOPS!!! Please provide a valid task number.");
        }
    }

    /**
     * Deletes the task identified by a user-supplied number.
     */
    private static void deleteTask(String argument, TaskList tasks, Ui ui) {
        if (argument.isBlank()) {
            ui.showError("OOPS!!! Please specify a task number.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(argument);
            if (!isExistingTaskNumber(taskNumber, tasks)) {
                ui.showError("OOPS!!! That task number does not exist.");
                return;
            }

            Task deletedTask = tasks.delete(taskNumber - 1);
            if (saveTasks(tasks, ui)) {
                ui.showTaskDeleted(deletedTask, tasks.size());
            } else {
                tasks.add(taskNumber - 1, deletedTask);
            }
        } catch (NumberFormatException e) {
            ui.showError("OOPS!!! Please provide a valid task number.");
        }
    }

    /**
     * Reports whether a one-based task number identifies an existing task.
     */
    private static boolean isExistingTaskNumber(int taskNumber, TaskList tasks) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Creates, saves, and displays a task described by an add command.
     */
    private static void addTask(String input, TaskList tasks, Ui ui) {
        Task task = createTask(input, ui);
        if (task == null) {
            return;
        }

        tasks.add(task);
        if (saveTasks(tasks, ui)) {
            ui.showTaskAdded(task, tasks.size());
        } else {
            tasks.delete(tasks.size() - 1);
        }
    }

    /**
     * Creates the task represented by an add command.
     *
     * @return the new task, or {@code null} when the command is invalid
     */
    private static Task createTask(String input, Ui ui) {
        if (input.startsWith("todo ")) {
            return createTodoTask(input.substring(5), ui);
        } else if (input.startsWith("deadline ")) {
            return createDeadlineTask(input, ui);
        } else if (input.startsWith("event ")) {
            return createEventTask(input, ui);
        }

        ui.showHelp();
        return null;
    }

    /**
     * Creates a to-do task from its description.
     */
    private static Task createTodoTask(String description, Ui ui) {
        if (description.isBlank()) {
            ui.showError("OOPS!!! The description of a todo cannot be empty.");
            return null;
        }
        return new TodoTask(description);
    }

    /**
     * Creates a deadline task from a command containing a {@code /by} value.
     */
    private static Task createDeadlineTask(String input, Ui ui) {
        String description = input.substring(9);
        if (description.isBlank()) {
            ui.showError("OOPS!!! The description of a deadline cannot be empty.");
            return null;
        }
        if (!input.contains(" /by ")) {
            ui.showError("OOPS!!! A deadline needs a /by date.");
            return null;
        }

        int byIndex = input.indexOf(" /by ");
        description = input.substring(9, byIndex);
        String by = input.substring(byIndex + 5);
        try {
            return new DeadlineTask(description, DeadlineTask.parseBy(by));
        } catch (DateTimeParseException e) {
            ui.showError("OOPS!!! Please use yyyy-MM-dd, yyyy-MM-dd HHmm, "
                    + "or d/M/yyyy HHmm for deadlines.");
            return null;
        }
    }

    /**
     * Creates an event task from a description and optional period details.
     */
    private static Task createEventTask(String input, Ui ui) {
        String description = input.substring(6);
        if (description.isBlank()) {
            ui.showError("OOPS!!! The description of an event cannot be empty.");
            return null;
        }

        String from = "";
        String to = "";
        if (input.contains(" /from ") && input.contains(" /to ")) {
            int fromIndex = input.indexOf(" /from ");
            int toIndex = input.indexOf(" /to ");
            description = input.substring(6, fromIndex);
            from = input.substring(fromIndex + 7, toIndex);
            to = input.substring(toIndex + 5);
        }
        return new EventTask(description, from, to);
    }

    /**
     * Loads stored tasks while allowing the chatbot to start if the data cannot be used.
     *
     * @param ui user interface used to show a loading error
     * @return loaded tasks, or an empty list when loading fails
     */
    private static TaskList loadTasks(Ui ui) {
        try {
            return new TaskList(Storage.load());
        } catch (IOException | IllegalArgumentException e) {
            ui.showError("OOPS!!! I could not load your saved tasks. Starting with an empty list.");
            return new TaskList();
        }
    }

    /**
     * Saves tasks and reports a failure without ending the chatbot.
     *
     * @param tasks tasks to save
     * @param ui user interface used to show a save error
     * @return {@code true} when the tasks were saved successfully
     */
    private static boolean saveTasks(TaskList tasks, Ui ui) {
        try {
            Storage.save(tasks.asList());
            return true;
        } catch (IOException e) {
            ui.showError("OOPS!!! I could not save your tasks. The change was not applied.");
            return false;
        }
    }

    /**
     * Restores a task's completion state after a failed save.
     *
     * @param task task to restore
     * @param wasDone completion state before the attempted change
     */
    private static void restoreStatus(Task task, boolean wasDone) {
        if (wasDone) {
            task.markAsDone();
        } else {
            task.markAsUndone();
        }
    }
}
