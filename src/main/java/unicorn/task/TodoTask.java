package unicorn.task;

/**
 * Represents a task without a date or time detail.
 */
public class TodoTask extends Task {
    /**
     * Creates a todo task.
     *
     * @param description task description
     */
    public TodoTask(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T] [" + getStatusIcon() + "] " + description;
    }
}
