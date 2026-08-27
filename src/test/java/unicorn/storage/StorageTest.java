package unicorn.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import unicorn.task.DeadlineTask;
import unicorn.task.EventTask;
import unicorn.task.Task;
import unicorn.task.TodoTask;

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

        TodoTask completedTodo = new TodoTask("read | book");
        completedTodo.markAsDone();
        List<Task> tasks = List.of(
                completedTodo,
                new DeadlineTask("return book", "June 6th"),
                new EventTask("project meeting", "Aug 6th", "2-4pm"));

        Storage.save(dataFile, tasks);

        List<String> expectedFileLines = List.of(
                "T | 1 | read \\| book",
                "D | 0 | return book | June 6th",
                "E | 0 | project meeting | Aug 6th | 2-4pm");
        List<String> actualFileLines = Files.readAllLines(dataFile);
        assertEqual(expectedFileLines, actualFileLines, "Saved task data did not match.");

        List<String> expectedTaskLines = List.of(
                "[T] [X] read | book",
                "[D] [ ] return book (by: June 6th)",
                "[E] [ ] project meeting (from: Aug 6th to: 2-4pm)");
        List<String> loadedTaskLines = Storage.load(dataFile).stream()
                .map(Task::toString)
                .toList();
        assertEqual(expectedTaskLines, loadedTaskLines, "Loaded task data did not match.");

        Path invalidDataFile = testDirectory.resolve("invalid.txt");
        Files.writeString(invalidDataFile, "D | 2 | return book | June 6th");
        assertInvalidTaskData(invalidDataFile);

        Path invalidDirectory = testDirectory.resolve("not-a-directory");
        Files.writeString(invalidDirectory, "This is a file, not a directory.");
        assertSaveFails(invalidDirectory.resolve("duke.txt"), tasks);
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

    /**
     * Checks that malformed stored data is rejected with a clear exception.
     *
     * @param dataFile file containing invalid task data
     * @throws IOException if the test cannot read the test file
     */
    private static void assertInvalidTaskData(Path dataFile) throws IOException {
        try {
            Storage.load(dataFile);
            throw new AssertionError("Invalid task data should not be loaded.");
        } catch (IllegalArgumentException e) {
            // Expected: the data file has an invalid completion status.
        }
    }

    /**
     * Checks that an unwritable storage location produces an I/O error.
     *
     * @param dataFile location that cannot be used as a file
     * @param tasks tasks to attempt to save
     */
    private static void assertSaveFails(Path dataFile, List<Task> tasks) {
        try {
            Storage.save(dataFile, tasks);
            throw new AssertionError("Saving to an invalid location should fail.");
        } catch (IOException e) {
            // Expected: a file cannot also be used as a parent directory.
        }
    }
}
