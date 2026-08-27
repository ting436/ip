import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Checks that task storage saves and reloads all supported task types.
 */
public class StorageTest {
    /**
     * Runs the storage checks without requiring an external test library.
     *
     * @param args command-line arguments, which are not used
     * @throws IOException if a storage operation fails
     */
    public static void main(String[] args) throws IOException {
        Path testDirectory = Files.createTempDirectory("unicorn-storage-test");
        Path dataFile = testDirectory.resolve("duke.txt");
        if (!Storage.load(dataFile).isEmpty()) {
            throw new AssertionError("A first run should have no tasks.");
        }

        TodoTask completedTodo = new TodoTask("read book");
        completedTodo.markAsDone();
        List<Task> tasks = List.of(
                completedTodo,
                new DeadlineTask("return book", "June 6th"),
                new EventTask("project meeting", "Aug 6th", "2-4pm"));

        Storage.save(dataFile, tasks);

        List<String> expectedFileLines = List.of(
                "T | 1 | read book",
                "D | 0 | return book | June 6th",
                "E | 0 | project meeting | Aug 6th | 2-4pm");
        List<String> actualFileLines = Files.readAllLines(dataFile);
        assertEqual(expectedFileLines, actualFileLines, "Saved task data did not match.");

        List<String> expectedTaskLines = List.of(
                "[T] [X] read book",
                "[D] [ ] return book (by: June 6th)",
                "[E] [ ] project meeting (from: Aug 6th to: 2-4pm)");
        List<String> loadedTaskLines = Storage.load(dataFile).stream()
                .map(Task::toString)
                .toList();
        assertEqual(expectedTaskLines, loadedTaskLines, "Loaded task data did not match.");
    }

    /**
     * Throws an error when two test values differ.
     *
     * @param expected expected value
     * @param actual actual value
     * @param message failure explanation
     */
    private static void assertEqual(List<String> expected, List<String> actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message);
        }
    }
}
