package allTasks;

public class Subtask extends Task {
    private final int ownerId;

    public Subtask(String title, String description, Status status, int ownerId) {
        super(title, description, status);
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }


    @Override
    public String toString() {
        return "allTasks.Subtask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}
