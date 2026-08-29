package unicorn.task;

/**
 * Represents a task that takes place over a stated period.
 */
public class EventTask extends Task {
    /** Detail describing when the event starts. */
    protected String from;
    /** Detail describing when the event ends. */
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

    /**
     * Returns a display representation of this event task.
     *
     * @return task status, description, and event period
     */
    @Override
    public String toString() {
        return "[E] [" + getStatusIcon() + "] "
                + description
                + " (from: " + from + " to: " + to + ")";
    }
}
