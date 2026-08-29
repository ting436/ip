package unicorn.task;

/**
 * Represents a task scheduled from one time detail to another.
 */
public class EventTask extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event task.
     *
     * @param description task description
     * @param from event start detail
     * @param to event end detail
     */
    public EventTask(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start detail.
     *
     * @return event start detail
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event end detail.
     *
     * @return event end detail
     */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E] [" + getStatusIcon() + "] "
                + description
                + " (from: " + from + " to: " + to + ")";
    }
}
