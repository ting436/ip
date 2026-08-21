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
                            (j + 1) + ": [" + listOfTasks[j].getStatusIcon() + "] "
                                    + listOfTasks[j].getDescription()
                    );
                }

            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                listOfTasks[taskNumber - 1].markAsDone();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println("[X] " + listOfTasks[taskNumber - 1].getDescription());

            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                listOfTasks[taskNumber - 1].markAsUndone();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("[ ] " + listOfTasks[taskNumber - 1].getDescription());

            } else {
                listOfTasks[i] = new Task(input);
                i++;
                System.out.println("added: " + input);
            }

            input = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
    }
}