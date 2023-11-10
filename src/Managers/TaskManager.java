package Managers;

import Model.Epic;
import Model.Subtask;
import Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    ArrayList<Object> getAllTasks();

    void clearAllTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(int id, Task task);

    void updateEpic(int id, Epic epic);

    void updateSubtask(int id, Subtask subtask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);

    TreeSet<Task> getPrioritizedTasks();

    List<Task> getHistory();

}
