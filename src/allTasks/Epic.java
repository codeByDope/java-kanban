package allTasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
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

    @Override
    public String toString() {
        return "allTasks.Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() + '\'' +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
