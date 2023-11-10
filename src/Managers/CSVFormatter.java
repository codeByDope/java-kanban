package Managers;

import Model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CSVFormatter {

    private CSVFormatter() { }

    public static String toString(Task task) {
        String result = "";
        if (task.getClass().equals(Task.class)) {
            result = String.format("%s,%s,%s,%s,%s,%s,%s,", task.getId(), TaskType.TASK, task.getTitle()
                    , task.getStatus(), task.getDescription(), task.getDuration(),formatLocalDateTime(task.getStartTime()));
        } else if (task.getClass().equals(Epic.class)) {
            result = String.format("%s,%s,%s,%s,%s,%s,%s,", task.getId(), TaskType.EPIC, task.getTitle()
                    , task.getStatus(), task.getDescription(), task.getDuration(),formatLocalDateTime(task.getStartTime()));
        } else if (task.getClass().equals(Subtask.class)) {
            result = String.format("%s,%s,%s,%s,%s,%s,%s,%s", task.getId(), TaskType.SUBTASK, task.getTitle(), task.getStatus(),
                    task.getDescription(), task.getDuration(),formatLocalDateTime(task.getStartTime()), ((Subtask) task).getEpicId());
        }
        return result;
    }

    private static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");
            return dateTime.format(formatter);
        }
        return null;
    }

    private static LocalDateTime parseDateTime(String value, DateTimeFormatter formatter) {
        return "null".equals(value) ? null : LocalDateTime.parse(value, formatter);
    }

    /**
     *  @param str   Params: str - формат id, type, name, status, description, epic, duration, startTime
     */
    public static Task fromString(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");
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
                return new Epic(id, name, description, status, Long.parseLong(parts[5]), parseDateTime(parts[6], formatter));
            case SUBTASK:
                return new Subtask(id, name, description, status, Long.parseLong(parts[5]), parseDateTime(parts[6], formatter), Integer.parseInt(parts[7]));
            default:
                return new Task(id, name, description, status, Long.parseLong(parts[5]), parseDateTime(parts[6], formatter));
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
