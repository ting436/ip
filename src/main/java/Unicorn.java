import java.io.IOException;
import java.util.Objects;

/**
 * Runs the Unicorn task chatbot.
 */
public class Unicorn {
    /**
     * Starts the chatbot, loading existing tasks and saving each successful list change.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = "          /\\\n"
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

        Ui ui = new Ui();
        TaskList listOfTasks = loadTasks(ui);

        ui.showWelcome(banner);

        String input = ui.readCommand();

        while (!Objects.equals(input, "bye")) {

            if (Objects.equals(input, "list")) {
                ui.showTaskList(listOfTasks);

            } else if (input.startsWith("mark ")) {
                String argument = input.substring(5);

                if (argument.isBlank()) {
                    ui.showError("OOPS!!! Please specify a task number.");
                } else {
                    try {
                        int taskNumber = Integer.parseInt(argument);

                        if (taskNumber < 1 || taskNumber > listOfTasks.size()) {
                            ui.showError("OOPS!!! That task number does not exist.");
                        } else {
                            Task task = listOfTasks.get(taskNumber - 1);
                            boolean wasDone = task.isDone();
                            task.markAsDone();
                            if (saveTasks(listOfTasks, ui)) {
                                ui.showTaskMarked(task);
                            } else {
                                restoreStatus(task, wasDone);
                            }
                        }

                    } catch (NumberFormatException e) {
                        ui.showError("OOPS!!! Please provide a valid task number.");
                    }
                }

            } else if (input.startsWith("unmark ")) {
                String argument = input.substring(7);

                if (argument.isBlank()) {
                    ui.showError("OOPS!!! Please specify a task number.");
                } else {
                    try {
                        int taskNumber = Integer.parseInt(argument);

                        if (taskNumber < 1 || taskNumber > listOfTasks.size()) {
                            ui.showError("OOPS!!! That task number does not exist.");
                        } else {
                            Task task = listOfTasks.get(taskNumber - 1);
                            boolean wasDone = task.isDone();
                            task.markAsUndone();
                            if (saveTasks(listOfTasks, ui)) {
                                ui.showTaskUnmarked(task);
                            } else {
                                restoreStatus(task, wasDone);
                            }
                        }

                    } catch (NumberFormatException e) {
                        ui.showError("OOPS!!! Please provide a valid task number.");
                    }
                }

            } else if (input.startsWith("delete ")) {
                String argument = input.substring(7);

                if (argument.isBlank()) {
                    ui.showError("OOPS!!! Please specify a task number.");
                } else {
                    try {
                        int taskNumber = Integer.parseInt(argument);

                        if (taskNumber < 1 || taskNumber > listOfTasks.size()) {
                            ui.showError("OOPS!!! That task number does not exist.");
                        } else {
                            Task deletedTask = listOfTasks.delete(taskNumber - 1);
                            if (saveTasks(listOfTasks, ui)) {
                                ui.showTaskDeleted(deletedTask, listOfTasks.size());
                            } else {
                                listOfTasks.add(taskNumber - 1, deletedTask);
                            }
                        }

                    } catch (NumberFormatException e) {
                        ui.showError("OOPS!!! Please provide a valid task number.");
                    }
                }

            } else {
                if (input.startsWith("todo ")) {
                    String description = input.substring(5);

                    if (description.isBlank()) {
                        ui.showError("OOPS!!! The description of a todo cannot be empty.");
                        input = ui.readCommand();
                        continue;
                    }

                    listOfTasks.add(new TodoTask(description));

                } else if (input.startsWith("deadline ")) {
                    String description = input.substring(9);

                    if (description.isBlank()) {
                        ui.showError("OOPS!!! The description of a deadline cannot be empty.");
                        input = ui.readCommand();
                        continue;
                    }

                    String by = "";

                    if (input.contains(" /by ")) {
                        int byIndex = input.indexOf(" /by ");
                        description = input.substring(9, byIndex);
                        by = input.substring(byIndex + 5);
                    }

                    listOfTasks.add(new DeadlineTask(description, by));

                } else if (input.startsWith("event ")) {
                    String description = input.substring(6);

                    if (description.isBlank()) {
                        ui.showError("OOPS!!! The description of an event cannot be empty.");
                        input = ui.readCommand();
                        continue;
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

                    listOfTasks.add(new EventTask(description, from, to));

                } else {
                    ui.showHelp();
                    input = ui.readCommand();
                    continue;
                }

                Task addedTask = listOfTasks.get(listOfTasks.size() - 1);
                if (saveTasks(listOfTasks, ui)) {
                    ui.showTaskAdded(addedTask, listOfTasks.size());
                } else {
                    listOfTasks.delete(listOfTasks.size() - 1);
                }
            }

            input = ui.readCommand();
        }

        ui.showGoodbye();
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
