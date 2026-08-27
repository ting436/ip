package unicorn.task;

public class DeadlineTask extends Task {
    protected String by;

    public DeadlineTask(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline detail.
     *
     * @return deadline detail
     */
    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D] [" + getStatusIcon() + "] "
                + description + " (by: " + by + ")";
    }
}
