package Managers;

import Exceptions.ManagerSaveException;
import Model.Epic;
import Model.Subtask;
import Model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    @Override
    public ArrayList<Object> getAllTasks() {
        return super.getAllTasks();
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        List<String> allStrs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                allStrs.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < allStrs.size() - 2; i++) {
            if (CSVFormatter.fromString(allStrs.get(i)) instanceof Subtask) {
                fileBackedTasksManager.addSubtask((Subtask) CSVFormatter.fromString(allStrs.get(i)));
            } else if (CSVFormatter.fromString(allStrs.get(i)) instanceof Epic) {
                fileBackedTasksManager.addEpic((Epic) CSVFormatter.fromString(allStrs.get(i)));
            } else {
                fileBackedTasksManager.addTask(CSVFormatter.fromString(allStrs.get(i)));
            }
        }

        for (int i : CSVFormatter.historyFromString(allStrs.get(allStrs.size() - 1))) {
            fileBackedTasksManager.addToHistoryById(i);
        }

        return fileBackedTasksManager;

    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
            for (Object o : getAllTasks()) {
                writer.write(CSVFormatter.toString((Task) o));
                writer.newLine();
            }
            writer.newLine();
            writer.write(CSVFormatter.historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new RuntimeException(new ManagerSaveException("Ошибка сохранения ", e));
        }
    }

    @Override
    public void addToHistoryById(int id) {
        super.addToHistoryById(id);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return super.getSubtaskById(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        return super.getSubtasksOfEpic(epic);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();

    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        save();
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        FileBackedTasksManager that = (FileBackedTasksManager) object;
        //проверить таски
        //проверить сабтаски
        //проверить эпики
        //проверить историю
        boolean tasksEqual = this.getAllTasks().equals(that.getAllTasks());
        boolean historyEqual = this.getHistory().equals(that.getHistory());
        return tasksEqual && historyEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
