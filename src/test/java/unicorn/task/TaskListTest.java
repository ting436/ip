package unicorn.task;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Checks the basic operations provided by {@link TaskList}.
 */
public class TaskListTest {
    /**
     * Verifies that tasks can be added, read, deleted, and reinserted.
     */
    @Test
    void taskListOperationsWork() {
        Task firstTask = new TodoTask("read book");
        Task secondTask = new DeadlineTask("return book", LocalDateTime.of(2019, 12, 2, 18, 0));
        TaskList tasks = new TaskList(List.of(firstTask));

        tasks.add(secondTask);
        assertEquals(2, tasks.size(), "Adding a task should increase the task count.");
        assertSame(secondTask, tasks.get(1), "The added task should be available by its index.");

        Task deletedTask = tasks.delete(0);
        assertSame(firstTask, deletedTask, "Deleting a task should return that task.");
        assertEquals(1, tasks.size(), "Deleting a task should decrease the task count.");

        tasks.add(0, deletedTask);
        assertEquals(2, tasks.size(), "Adding at an index should restore a deleted task.");
        assertEquals(2, tasks.asList().size(), "The task snapshot should include all tasks.");
    }
}
