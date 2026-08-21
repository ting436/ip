import java.util.Objects;
import java.util.Scanner;

public class Unicorn {
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

        Scanner scanner = new Scanner(System.in);
        Task[] listOfTasks = new Task[100];
        int i = 0;

        System.out.println(banner);
        System.out.println("Hello! I'm Unicorn.");
        System.out.println("What can I do for you?");

        String input = scanner.nextLine();

        while (!Objects.equals(input, "bye")) {

            if (Objects.equals(input, "list")) {
                for (int j = 0; j < i; j++) {
                    System.out.println(
                            (j + 1) + ": " + listOfTasks[j].toString()
                    );
                }

            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                listOfTasks[taskNumber - 1].markAsDone();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println(listOfTasks[taskNumber - 1].toString());

            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                listOfTasks[taskNumber - 1].markAsUndone();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(listOfTasks[taskNumber - 1].toString());

            } else {
                if (input.startsWith("todo ")) {
                    String description = input.substring(5);
                    listOfTasks[i] = new Task(description, TaskType.TODO);

                } else if (input.startsWith("deadline ")) {
                    String description = input.substring(9);
                    listOfTasks[i] = new Task(description, TaskType.DEADLINE);

                } else if (input.startsWith("event ")) {
                    String description = input.substring(6);
                    listOfTasks[i] = new Task(description, TaskType.EVENT);

                } else {
                    System.out.println("I don't understand that command.");
                    input = scanner.nextLine();
                    continue;
                }

                i++;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + listOfTasks[i - 1]);
                System.out.println("Now you have " + i + " tasks in the list.");
            }

            input = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
    }
}