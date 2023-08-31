package allTasks;

public class Task {
    protected int id;
    protected final String title;
    protected final String description;
    protected Status status;
    private static int count = 0;

    public Task(String title, String description) {
        id = generatedId();
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    private int generatedId() {
        return ++count;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
