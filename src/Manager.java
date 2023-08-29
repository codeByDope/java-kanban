import allTasks.Epic;
import allTasks.Subtask;
import allTasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    int id;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Subtask> subtasks;
    HashMap<Integer, Epic> epics;

    public Manager(HashMap<Integer, Task> tasks, HashMap<Integer, Subtask> subtasks, HashMap<Integer, Epic> epics) {
        this.id = 0;
        this.tasks = tasks;
        this.subtasks = subtasks;
        this.epics = epics;
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
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    public void addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    public void addSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        ArrayList<Integer> ownersSubtaskIdsList = epics.get(subtask.getOwnerId()).getSubtasksIds();
        ownersSubtaskIdsList.add(id);
        epics.get(subtask.getOwnerId()).setSubtasksIds(ownersSubtaskIdsList);
        epics.get(subtask.getOwnerId()).checkAndRefreshStatus(getSubtasksOfEpic(epics.get(subtask.getOwnerId())));
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
        epics.get(subtask.getOwnerId()).checkAndRefreshStatus(getSubtasksOfEpic(epics.get(subtask.getOwnerId())));
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        ArrayList<Integer> subtasksIds = epics.get(id).getSubtasksIds();
        for (int i = 0; i < subtasksIds.size(); i++) {
            subtasks.remove(i+1);
        }

        epics.remove(id);
    }

    public void removeSubtask(int id) {
        ArrayList<Integer> ownersSubtaskIdsList = epics.get(subtasks.get(id).getOwnerId()).getSubtasksIds();
        ownersSubtaskIdsList.remove(Integer.valueOf(id));
        epics.get(subtasks.get(id).getOwnerId()).setSubtasksIds(ownersSubtaskIdsList);
        epics.get(subtasks.get(id).getOwnerId()).checkAndRefreshStatus(getSubtasksOfEpic(epics.get(subtasks.get(id).getOwnerId())));
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
