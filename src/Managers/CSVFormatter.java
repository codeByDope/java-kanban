package Managers;

import Model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CSVFormatter {

    private CSVFormatter() { }

    public static String toString(Task task) {
        String result = "";
        if (task.getClass().equals(Task.class)) {
            result = String.format("%s,%s,%s,%s,%s,", task.getId(), TaskType.TASK, task.getTitle()
                    , task.getStatus(), task.getDescription());
        } else if (task.getClass().equals(Epic.class)) {
            result = String.format("%s,%s,%s,%s,%s,", task.getId(), TaskType.EPIC, task.getTitle()
                    , task.getStatus(), task.getDescription());
        } else if (task.getClass().equals(Subtask.class)) {
            result = String.format("%s,%s,%s,%s,%s,%s", task.getId(), TaskType.SUBTASK, task.getTitle()
                    , task.getStatus(), task.getDescription(), ((Subtask) task).getEpicId());
        }
        return result;
    }

    /**
     *  @param str   Params: str - формат id, type, name, status, description, epic
     */
    public static Task fromString(String str) {
        String[] parts = str.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        if (id > Task.getCount()) {
            Task.setCount(id);
        }
        switch (type) {
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                return new Subtask(id, name, description, status, Integer.parseInt(parts[5]));
            default:
                return new Task(id, name, description, status);
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder result = new StringBuilder();
        for (Task e : manager.getHistory()) {
            result.append(e.getId()).append(",");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    public static List<Integer> historyFromString(String str) {
        String[] parts = str.split(",");
        List<Integer> result = new ArrayList<>();
        for (String e : parts) {
            result.add(Integer.parseInt(e));
        }
        return result;
    }
}
