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
        int i = 0;

        System.out.println(banner);
        System.out.println("Hello! I'm Unicorn.");
        System.out.println("What can I do for you?");

        String input = scanner.nextLine();

        while (!Objects.equals(input, "bye")) {

            if (Objects.equals(input, "list")) {
                for (int j = 0; j < i; j++) {
                    System.out.println((j + 1) + ": " + listOfInputs[j]);
                }
            } else {
                listOfInputs[i] = input;
                i++;
                System.out.println("added: " + input);
            }

            input = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
    }
}