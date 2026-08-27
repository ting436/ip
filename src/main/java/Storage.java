import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Saves and loads the application's task data.
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
        save(DATA_FILE, tasks);
    }

    /**
     * Writes tasks to a specified file. This overload keeps automated tests
     * separate from the user's data file.
     *
     * @param dataFile file to write
     * @param tasks tasks to save
     * @throws IOException if the directory or file cannot be written
     */
    static void save(Path dataFile, List<Task> tasks) throws IOException {
        Files.createDirectories(dataFile.getParent());
        List<String> taskLines = tasks.stream()
                .map(Storage::toFileString)
                .toList();
        Files.write(dataFile, taskLines);
    }

    /**
     * Loads tasks from {@code ./data/duke.txt}. A first-time user has no data file,
     * so an empty task list is returned in that case.
     *
     * @return the saved tasks in their saved order, or an empty list for a first run
     * @throws IOException if an existing data file cannot be read
     */
    public static List<Task> load() throws IOException {
        return load(DATA_FILE);
    }

    /**
     * Loads tasks from a specified file.
     *
     * @param dataFile file to read
     * @return saved tasks, or an empty list when the file does not yet exist
     * @throws IOException if an existing file cannot be read
     */
    static List<Task> load(Path dataFile) throws IOException {
        if (Files.notExists(dataFile)) {
            return List.of();
        }
        return Files.readAllLines(dataFile).stream()
                .map(Storage::toTask)
                .toList();
    }

    /**
     * Converts a task to a line in the storage format.
     *
     * @param task task to convert
     * @return task type, completion status, and task details separated by vertical bars
     */
    private static String toFileString(Task task) {
        String completed = task.isDone ? "1" : "0";
        if (task instanceof TodoTask) {
            return "T | " + completed + " | " + task.description;
        } else if (task instanceof DeadlineTask deadlineTask) {
            return "D | " + completed + " | " + task.description + " | " + deadlineTask.by;
        } else if (task instanceof EventTask eventTask) {
            return "E | " + completed + " | " + task.description + " | "
                    + eventTask.from + " | " + eventTask.to;
        }
        throw new IllegalArgumentException("Unsupported task type");
    }

    /**
     * Recreates a task from one line in the storage format.
     *
     * @param taskLine task data read from the file
     * @return the task represented by the line
     */
    private static Task toTask(String taskLine) {
        String[] parts = taskLine.split(" \\| ");
        Task task = switch (parts[0]) {
        case "T" -> new TodoTask(parts[2]);
        case "D" -> new DeadlineTask(parts[2], parts[3]);
        case "E" -> new EventTask(parts[2], parts[3], parts[4]);
        default -> throw new IllegalArgumentException("Unsupported task type");
        };
        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
