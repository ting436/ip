import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Saves the current task list to the application's data file.
 * Reading the file is intentionally added in a later increment.
 */
public class Storage {
    private static final Path DATA_FILE = Path.of("data", "duke.txt");

    /**
     * Writes one task per line to {@code ./data/duke.txt}.
     *
     * @param tasks tasks to save
     * @throws IOException if the data directory or file cannot be written
     */
    public static void save(List<Task> tasks) throws IOException {
        Files.createDirectories(DATA_FILE.getParent());
        List<String> taskLines = tasks.stream()
                .map(Task::toString)
                .toList();
        Files.write(DATA_FILE, taskLines);
    }
}
