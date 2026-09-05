package unicorn.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the tasks currently managed by the chatbot.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks tasks with which to initialise the list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Inserts a task at a specified zero-based position.
     *
     * @param index position at which to insert the task
     * @param task task to add
     */
    public void add(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Returns a task at a specified zero-based position.
     *
     * @param index position of the task
     * @return the requested task
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Deletes and returns the task at a specified zero-based position.
     *
     * @param index position of the task to delete
     * @return the deleted task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Finds tasks whose descriptions contain a keyword, ignoring letter case.
     *
     * @param keyword text to search for in task descriptions
     * @return matching tasks in their original list order
     */
    public List<Task> find(String keyword) {
        String normalizedKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(normalizedKeyword))
                .toList();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an immutable snapshot for operations such as saving.
     *
     * @return current tasks in list order
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
