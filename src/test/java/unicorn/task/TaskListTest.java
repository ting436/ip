package unicorn.task;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Checks the basic operations provided by {@link TaskList}.
 */
public class TaskListTest {
    /**
     * Runs the task-list checks without requiring an external test library.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Task firstTask = new TodoTask("read book");
        Task secondTask = new DeadlineTask("return book", LocalDateTime.of(2019, 12, 2, 18, 0));
        TaskList tasks = new TaskList(List.of(firstTask));

        tasks.add(secondTask);
        assertEqual(2, tasks.size(), "Adding a task should increase the task count.");
        assertSame(secondTask, tasks.get(1), "The added task should be available by its index.");

        Task deletedTask = tasks.delete(0);
        assertSame(firstTask, deletedTask, "Deleting a task should return that task.");
        assertEqual(1, tasks.size(), "Deleting a task should decrease the task count.");

        tasks.add(0, deletedTask);
        assertEqual(2, tasks.size(), "Adding at an index should restore a deleted task.");
        assertEqual(2, tasks.asList().size(), "The task snapshot should include all tasks.");
    }

    /**
     * Checks that two integers are equal.
     *
     * @param expected expected value
     * @param actual actual value
     * @param message failure explanation
     */
    private static void assertEqual(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message);
        }
    }

    /**
     * Checks that two references point to the same object.
     *
     * @param expected expected object
     * @param actual actual object
     * @param message failure explanation
     */
    private static void assertSame(Task expected, Task actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message);
        }
    }
}
