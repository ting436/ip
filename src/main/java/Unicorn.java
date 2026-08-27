import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Runs the Unicorn task chatbot.
 */
public class Unicorn {
    /**
     * Starts the chatbot, loading existing tasks and saving each successful list change.
     *
     * @param args command-line arguments, which are not used
     * @throws IOException if the task data file cannot be read or written
     */
    public static void main(String[] args) throws IOException {
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

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> listOfTasks = new ArrayList<>(Storage.load());

        System.out.println(banner);
        System.out.println("Hello! I'm Unicorn.");
        System.out.println("What can I do for you?");

        String input = scanner.nextLine();

        while (!Objects.equals(input, "bye")) {

            if (Objects.equals(input, "list")) {
                for (int j = 0; j < listOfTasks.size(); j++) {
                    System.out.println((j + 1) + ": " + listOfTasks.get(j));
                }

            } else if (input.startsWith("mark ")) {
                String argument = input.substring(5);

                if (argument.isBlank()) {
                    System.out.println("OOPS!!! Please specify a task number.");
                } else {
                    try {
                        int taskNumber = Integer.parseInt(argument);

                        if (taskNumber < 1 || taskNumber > listOfTasks.size()) {
                            System.out.println("OOPS!!! That task number does not exist.");
                        } else {
                            listOfTasks.get(taskNumber - 1).markAsDone();
                            Storage.save(listOfTasks);

                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println(listOfTasks.get(taskNumber - 1));
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("OOPS!!! Please provide a valid task number.");
                    }
                }

            } else if (input.startsWith("unmark ")) {
                String argument = input.substring(7);

                if (argument.isBlank()) {
                    System.out.println("OOPS!!! Please specify a task number.");
                } else {
                    try {
                        int taskNumber = Integer.parseInt(argument);

                        if (taskNumber < 1 || taskNumber > listOfTasks.size()) {
                            System.out.println("OOPS!!! That task number does not exist.");
                        } else {
                            listOfTasks.get(taskNumber - 1).markAsUndone();
                            Storage.save(listOfTasks);

                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println(listOfTasks.get(taskNumber - 1));
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("OOPS!!! Please provide a valid task number.");
                    }
                }

            } else if (input.startsWith("delete ")) {
                String argument = input.substring(7);

                if (argument.isBlank()) {
                    System.out.println("OOPS!!! Please specify a task number.");
                } else {
                    try {
                        int taskNumber = Integer.parseInt(argument);

                        if (taskNumber < 1 || taskNumber > listOfTasks.size()) {
                            System.out.println("OOPS!!! That task number does not exist.");
                        } else {
                            Task deletedTask = listOfTasks.remove(taskNumber - 1);
                            Storage.save(listOfTasks);

                            System.out.println("Noted. I've removed this task:");
                            System.out.println("  " + deletedTask);
                            System.out.println("Now you have "
                                    + listOfTasks.size() + " tasks in the list.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("OOPS!!! Please provide a valid task number.");
                    }
                }

            } else {
                if (input.startsWith("todo ")) {
                    String description = input.substring(5);

                    if (description.isBlank()) {
                        System.out.println(
                                "OOPS!!! The description of a todo cannot be empty.");
                        input = scanner.nextLine();
                        continue;
                    }

                    listOfTasks.add(new TodoTask(description));

                } else if (input.startsWith("deadline ")) {
                    String description = input.substring(9);

                    if (description.isBlank()) {
                        System.out.println(
                                "OOPS!!! The description of a deadline cannot be empty.");
                        input = scanner.nextLine();
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
                        System.out.println(
                                "OOPS!!! The description of an event cannot be empty.");
                        input = scanner.nextLine();
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
                    System.out.println(
                            "I don't understand that command. You may add tasks by specifying todo, "
                                    + "event, or deadline at the start, or view your tasks by entering 'list'. "
                                    + "You may also mark, unmark, or delete your tasks by specifying "
                                    + "'mark', 'unmark', or 'delete' followed by the index of the task.");
                    input = scanner.nextLine();
                    continue;
                }

                System.out.println("Got it. I've added this task:");
                Storage.save(listOfTasks);
                System.out.println("  " + listOfTasks.get(listOfTasks.size() - 1));
                System.out.println("Now you have "
                        + listOfTasks.size() + " tasks in the list.");
            }

            input = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
    }
}
