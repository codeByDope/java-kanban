import allTasks.Epic;
import allTasks.Status;
import allTasks.Subtask;
import allTasks.Task;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        Manager manager = getManager();

        System.out.println(manager.getAllTasks());

        Subtask subtaskUpd1 = new Subtask("Саб таск 2", "я в шоке", Status.NEW, 1);
        Subtask subtaskUpd2 = new Subtask("Саб таск 3", "я в шоке333", Status.IN_PROGRESS, 4);
        Task taskUpd2 = new Task("Name2", "Description2", Status.NEW);
        manager.updateTask(7, taskUpd2);
        manager.updateSubtask(3 ,subtaskUpd1);
        manager.updateSubtask(5, subtaskUpd2);

        System.out.println(manager.getAllTasks());

        manager.removeEpic(1);
        manager.removeTask(6);
        System.out.println(manager.getAllTasks());

        manager.clearAllTasks();
        System.out.println(manager.getAllTasks());
    }

    private static Manager getManager() {
        Manager manager = new Manager(new HashMap<>(), new HashMap<>(), new HashMap<>());

        Epic epic = new Epic("Епик", "Ек макарек");
        Subtask subtask1 = new Subtask("Саб таск 1", "опять 25", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Саб таск 2", "я в шоке", Status.DONE, 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic1 = new Epic("Епик1", "Ек макарек1");
        manager.addEpic(epic1);

        Subtask subtask3 = new Subtask("Саб таск 3", "я в шоке333", Status.DONE, 4);
        manager.addSubtask(subtask3);

        Task task1 = new Task("Name", "Description", Status.DONE);
        Task task2 = new Task("Name2", "Description2", Status.IN_PROGRESS);
        manager.addTask(task1);
        manager.addTask(task2);
        return manager;
    }
}
