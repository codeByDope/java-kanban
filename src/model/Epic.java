package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description);
        subtasksIds = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public Epic(int id, String title, String description, Status status, long duration, LocalDateTime startTime) {
        super(id, title, description, status, duration, startTime);
        subtasksIds = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        subtasksIds = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }
    public void setSubtasksIds(ArrayList subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    public void checkAndRefreshStatus(ArrayList<Subtask> subtasks) {
        int statusDoneCounter = 0;
        int statusInProgressCounter = 0;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                statusInProgressCounter++;
            } else if (subtask.getStatus() == Status.DONE) {
                statusDoneCounter++;
            }
        }

        if (statusDoneCounter == 0 && statusInProgressCounter == 0) {
            setStatus(Status.NEW);
        } else if (statusInProgressCounter > 0 || (statusDoneCounter > 0 && statusDoneCounter < subtasks.size())) {
            setStatus(Status.IN_PROGRESS);
        } else {
            setStatus(Status.DONE);
        }
    }

    public void addSubtask(int SubtaskId) {
        subtasksIds.add(SubtaskId);
    }

    public void removeSubtask(int SubtaskId) {
        subtasksIds.remove(Integer.valueOf(SubtaskId));
    }

    public void setEpicDuration(ArrayList<Subtask> subtasks) {
        long duration = 0;
        for (Subtask subtask: subtasks) {
            duration += subtask.getDuration();
        }
        setDuration(duration);
    }

    public void setEpicStartTime(ArrayList<Subtask> subtasks) {
        LocalDateTime startTime = LocalDateTime.MAX;
        for (Subtask subtask: subtasks) {
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
        }
        setStartTime(startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return getStartTime().plusMinutes(getDuration());
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", subtasksIds=" + subtasksIds +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Epic epic = (Epic) object;
        return Objects.equals(subtasksIds, epic.subtasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksIds);
    }
}
