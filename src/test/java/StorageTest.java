import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Checks that {@link Storage} writes the current task list in display order.
 */
public class StorageTest {
    /**
     * Runs the storage happy-path check without requiring an external test library.
     *
     * @param args command-line arguments, which are not used
     * @throws IOException if the test cannot read the saved data file
     */
    public static void main(String[] args) throws IOException {
        TodoTask completedTodo = new TodoTask("read book");
        completedTodo.markAsDone();
        List<Task> tasks = List.of(
                completedTodo,
                new DeadlineTask("return book", "June 6th"));

        Storage.save(tasks);

        List<String> expectedLines = List.of(
                "[T] [X] read book",
                "[D] [ ] return book (by: June 6th)");
        List<String> actualLines = Files.readAllLines(Path.of("data", "duke.txt"));
        if (!expectedLines.equals(actualLines)) {
            throw new AssertionError("Saved tasks did not match the task list.");
        }
    }
}
