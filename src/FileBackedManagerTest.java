import Managers.FileBackedTasksManager;
import Managers.Managers;
import Model.Epic;
import Model.Subtask;
import Model.Task;

import java.io.File;

public class FileBackedManagerTest {
    public static void main(String[] args) {
        File file = new File("src/Resources/FileBackedTasks.txt");

        Epic epic = new Epic("Епик", "Ек макарек");
        Subtask subtask1 = new Subtask("Саб таск 1", "опять 25", 1);
        Task task = new Task("Task", "я в шоке");

        FileBackedTasksManager tasksManager1 = Managers.getFileManager(file);
        tasksManager1.addEpic(epic);
        tasksManager1.addSubtask(subtask1);
        tasksManager1.addTask(task);

        tasksManager1.getTaskById(3);
        tasksManager1.save();
        tasksManager1.getSubtaskById(2);
        tasksManager1.save();
        System.out.println(tasksManager1.getAllTasks());
        System.out.println(tasksManager1.getHistory());
//
//        System.out.println();
//
//        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);
//
//        System.out.println(tasksManager2.getAllTasks());
//
//        System.out.println(tasksManager2.getHistory());
    }
}
