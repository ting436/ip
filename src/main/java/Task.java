public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsUndone() {
        isDone = false;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        String typeIcon;

        if (type == TaskType.TODO) {
            typeIcon = "T";
        } else if (type == TaskType.DEADLINE) {
            typeIcon = "D";
        } else {
            typeIcon = "E";
        }

        return "[" + typeIcon + "][" + getStatusIcon() + "] " + description;
    }
}