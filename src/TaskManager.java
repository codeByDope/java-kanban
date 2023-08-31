import allTasks.Epic;
import allTasks.Subtask;
import allTasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

    public TaskManager() {

        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public ArrayList<Object> getAllTasks() {
        ArrayList<Object> allTasks = new ArrayList<>();

        for (Integer key : tasks.keySet()) {
            allTasks.add(tasks.get(key));
        }
        for (Integer key : epics.keySet()) {
            allTasks.add(epics.get(key));
        }
        for (Integer key : subtasks.keySet()) {
            allTasks.add(subtasks.get(key));
        }

        return allTasks;
    }

    public void clearAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else {
            System.out.println("Ни-че-го");
            return null;
        }
    }

    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.println("Ни-че-го");
            return null;
        }
    }

    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            System.out.println("Ни-че-го");
            return null;
        }
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
    }

    public void updateTask(int id, Task task) {
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateEpic(int id, Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
    }

    public void updateSubtask(int id, Subtask subtask) {
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).checkAndRefreshStatus(getSubtasksOfEpic(epics.get(subtask.getEpicId())));
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        ArrayList<Integer> subtasksIds = epics.get(id).getSubtasksIds();
        for (int subtaskId: subtasksIds) {
            subtasks.remove(subtaskId);
        }

        epics.remove(id);
    }

    public void removeSubtask(int id) {
        epics.get(subtasks.get(id).getEpicId()).removeSubtask(subtasks.get(id).getId());
        epics.get(subtasks.get(id).getEpicId()).checkAndRefreshStatus(getSubtasksOfEpic(epics.get(subtasks.get(id).getEpicId())));
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtaskOfEpic = new ArrayList<>();

        for (int subtaskId : epic.getSubtasksIds()) {
            subtaskOfEpic.add(subtasks.get(subtaskId));
        }
        return subtaskOfEpic;
    }


}
