package unicorn;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

import unicorn.storage.Storage;
import unicorn.task.DeadlineTask;
import unicorn.task.EventTask;
import unicorn.task.Task;
import unicorn.task.TaskList;
import unicorn.task.TodoTask;

/**
 * Processes commands for the Unicorn task chatbot.
 */
public class Unicorn {
    private static final String HELP_MESSAGE = "That signal was unclear. Every great quest needs a map! "
            + "Add a quest with todo, event, or deadline; view quests with 'list' or 'find'; "
            + "or update them with 'mark', 'unmark', or 'delete' followed by the quest number.";
    private static final String WELCOME_MESSAGE = "Hello! I'm Prisma, your wise tech unicorn. "
            + "Tell me what is on your quest list, and we'll make some magic.";

    private final TaskList tasks;
    private final TaskSaver taskSaver;
    private String commandType;

    /**
     * Creates a chatbot using tasks loaded from the default data file.
     */
    public Unicorn() {
        this(loadTasks(), Storage::save);
    }

    /**
     * Creates a chatbot with supplied tasks and persistence behavior.
     *
     * @param tasks initial tasks managed by the chatbot
     * @param taskSaver operation used to save task changes
     */
    Unicorn(TaskList tasks, TaskSaver taskSaver) {
        assert tasks != null : "Task list must not be null";
        assert taskSaver != null : "Task saver must not be null";

        this.tasks = tasks;
        this.taskSaver = taskSaver;
    }

    /**
     * Processes a user command and returns the chatbot's response.
     *
     * @param input command entered by the user
     * @return response describing the command result
     */
    public String getResponse(String input) {
        assert input != null : "Input must not be null";
        commandType = input.split(" ")[0];

        if (input.equals("list")) {
            return formatTasks(tasks.asList());
        } else if (input.startsWith("find ")) {
            return findTasks(input.substring(5));
        } else if (input.startsWith("mark ")) {
            return setTaskCompletion(input.substring(5), true);
        } else if (input.startsWith("unmark ")) {
            return setTaskCompletion(input.substring(7), false);
        } else if (input.startsWith("delete ")) {
            return deleteTask(input.substring(7));
        } else if (input.startsWith("todo ")) {
            return addTodo(input.substring(5));
        } else if (input.startsWith("deadline ")) {
            return addDeadline(input);
        } else if (input.startsWith("event ")) {
            return addEvent(input);
        } else if (input.equals("bye")) {
            return "Keep shining! Prisma will be here when your next quest begins.";
        }
        return HELP_MESSAGE;
    }

    /**
     * Returns Prisma's introductory greeting.
     *
     * @return welcome message that introduces the chatbot's personality
     */
    public String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }

    public String getCommandType() {
        return commandType;
    }

    private String findTasks(String keyword) {
        if (keyword.isBlank()) {
            return "My unicorn senses need a keyword before they can search.";
        }
        List<Task> matchingTasks = tasks.find(keyword);
        return "My unicorn senses found these matching quests:\n" + formatTasks(matchingTasks);
    }

    private String setTaskCompletion(String argument, boolean isDone) {
        Task task = getTask(argument);
        if (task == null) {
            return getTaskNumberError(argument);
        }

        boolean wasDone = task.isDone();
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsUndone();
        }

        if (!saveTasks()) {
            restoreStatus(task, wasDone);
            return getSaveError();
        }
        if (isDone) {
            return "Brilliant work—another quest conquered!\n" + task;
        }
        return "Quest reopened. Even wise adventurers revise their plans.\n" + task;
    }

    private String deleteTask(String argument) {
        Integer taskIndex = parseTaskIndex(argument);
        if (taskIndex == null) {
            return getTaskNumberError(argument);
        }

        Task deletedTask = tasks.delete(taskIndex);
        if (!saveTasks()) {
            tasks.add(taskIndex, deletedTask);
            return getSaveError();
        }
        return "Poof! This quest has left the digital realm:\n  " + deletedTask
                + "\nYour quest log now holds " + tasks.size() + " quests.";
    }

    private String addTodo(String description) {
        if (description.isBlank()) {
            return "A quest needs a description before I can weave it into the list.";
        }
        return addTask(new TodoTask(description));
    }

    private String addDeadline(String input) {
        int byIndex = input.indexOf(" /by ");
        if (byIndex < 0) {
            return "My foresight needs a /by date for that deadline quest.";
        }

        String description = input.substring(9, byIndex);
        String by = input.substring(byIndex + 5);
        if (description.isBlank()) {
            return "A deadline quest needs a description before I can save it.";
        }
        try {
            return addTask(new DeadlineTask(description, DeadlineTask.parseBy(by)));
        } catch (DateTimeParseException e) {
            return "A little time glitch! Please use yyyy-MM-dd, yyyy-MM-dd HHmm, "
                    + "or d/M/yyyy HHmm for deadlines.";
        }
    }

    private String addEvent(String input) {
        int fromIndex = input.indexOf(" /from ");
        int toIndex = input.indexOf(" /to ");
        if (fromIndex < 0 || toIndex < fromIndex) {
            return "My event compass needs both /from and /to details.";
        }

        String description = input.substring(6, fromIndex);
        String from = input.substring(fromIndex + 7, toIndex);
        String to = input.substring(toIndex + 5);
        if (description.isBlank()) {
            return "An event quest needs a description before I can save it.";
        }
        return addTask(new EventTask(description, from, to));
    }

    private String addTask(Task task) {
        tasks.add(task);
        if (!saveTasks()) {
            tasks.delete(tasks.size() - 1);
            return getSaveError();
        }
        return "Your quest has been added to the rainbow:\n  " + task
                + "\nYour quest log now holds " + tasks.size() + " quests.";
    }

    private Task getTask(String argument) {
        Integer taskIndex = parseTaskIndex(argument);
        return taskIndex == null ? null : tasks.get(taskIndex);
    }

    private Integer parseTaskIndex(String argument) {
        if (argument.isBlank()) {
            return null;
        }
        try {
            int taskIndex = Integer.parseInt(argument) - 1;
            return taskIndex >= 0 && taskIndex < tasks.size() ? taskIndex : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getTaskNumberError(String argument) {
        if (argument.isBlank()) {
            return "My quest compass needs a quest number.";
        }
        try {
            Integer.parseInt(argument);
            return "That quest number has not appeared in this realm yet.";
        } catch (NumberFormatException e) {
            return "That signal is not a valid quest number.";
        }
    }

    private boolean saveTasks() {
        try {
            taskSaver.save(tasks.asList());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static TaskList loadTasks() {
        try {
            return new TaskList(Storage.load());
        } catch (IOException | IllegalArgumentException e) {
            return new TaskList();
        }
    }

    private static String formatTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "Your quest log is clear. Enjoy the breathing room!";
        }
        StringBuilder response = new StringBuilder();
        for (int index = 0; index < tasks.size(); index++) {
            if (index > 0) {
                response.append('\n');
            }
            response.append(index + 1).append(". ").append(tasks.get(index));
        }
        return response.toString();
    }

    private static void restoreStatus(Task task, boolean wasDone) {
        if (wasDone) {
            task.markAsDone();
        } else {
            task.markAsUndone();
        }
    }

    private static String getSaveError() {
        return "A little glitch disturbed the magic. I could not save your quests, so nothing changed.";
    }

    /**
     * Saves the current tasks to persistent storage.
     */
    @FunctionalInterface
    interface TaskSaver {
        void save(List<Task> tasks) throws IOException;
    }
}
