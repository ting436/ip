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
        String[] listOfInputs = new String[100];
        int[] listOfInputStatuses = new int[100];
        int i = 0;

        System.out.println(banner);
        System.out.println("Hello! I'm Unicorn.");
        System.out.println("What can I do for you?");

        String input = scanner.nextLine();

        while (!Objects.equals(input, "bye")) {

            if (Objects.equals(input, "list")) {
                for (int j = 0; j < i; j++) {
                    if (listOfInputStatuses[j] == 0) {
                        System.out.print("[ ] ");
                    } else {
                        System.out.print("[X] ");
                    }
                    System.out.println((j + 1) + ": " + listOfInputs[j]);
                }

            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                listOfInputStatuses[taskNumber - 1] = 1;

                System.out.println("Nice! I've marked this task as done:");
                System.out.println("[X] " + listOfInputs[taskNumber - 1]);

            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                listOfInputStatuses[taskNumber - 1] = 0;

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("[ ] " + listOfInputs[taskNumber - 1]);

            } else {
                listOfInputs[i] = input;
                listOfInputStatuses[i] = 0;
                i++;
                System.out.println("added: " + input);
            }

            input = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
    }
}