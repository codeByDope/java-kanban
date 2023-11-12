package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected int id;
    protected final String title;
    protected final String description;
    protected Status status;
    private static int count = 0;
    protected long duration;
    protected TaskType taskType;
    protected LocalDateTime startTime;

    public Task(String title, String description) {
        id = generatedId();
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.taskType = TaskType.TASK;
    }

    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public Task(int id, String title, String description, Status status, long duration, LocalDateTime startTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public static void setCount(int count) {
        Task.count = count;
    }

    public static int getCount() {
        return Task.count;
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

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public static void resetStatics() {
        count = 0;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public boolean hasTimeOverlap(Task otherTask) {
        LocalDateTime startA = this.getStartTime();
        LocalDateTime endA = this.getEndTime();
        LocalDateTime startB = otherTask.getStartTime();
        LocalDateTime endB = otherTask.getEndTime();

        return startA.isBefore(endB) && endA.isAfter(startB);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status);
    }
}
