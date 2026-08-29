package unicorn.task;

/**
 * Represents a task without a date or event period.
 */
public class TodoTask extends Task {
    /**
     * Creates a to-do task.
     *
     * @param description task description
     */
    public TodoTask(String description) {
        super(description);
    }

    /**
     * Returns a display representation of this to-do task.
     *
     * @return task status and description
     */
    @Override
    public String toString() {
        return "[T] [" + getStatusIcon() + "] " + description;
    }
}
