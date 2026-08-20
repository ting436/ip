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

        System.out.println(banner);
        System.out.println("Hello! I'm Unicorn.");
        System.out.println("What can I do for you?");
        String input = scanner.nextLine();

        while (!Objects.equals(input, "bye")) {
            System.out.println(input);
            input = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
    }
}
