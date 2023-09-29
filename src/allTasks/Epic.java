package allTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description);
        subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
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

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
