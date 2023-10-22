import managers.FileBackedTasksManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;

import static managers.FileBackedTasksManager.loadFromFile;

public class FileBackedManagerTest {
    public static void main(String[] args) {
        File file = new File("src/resources/FileBackedTasks.txt");
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        fileManager.addTask(new Task("task1", "Купить автомобиль"));
        fileManager.addEpic(new Epic("new Epic1", "Новый Эпик"));
        fileManager.addSubtask(new Subtask("New Subtask", "Подзадача", 2));
        fileManager.addSubtask(new Subtask("New Subtask2", "Подзадача2", 2));
        fileManager.getTaskById(1);
        fileManager.getEpicById(2);
        fileManager.getSubtaskById(3);
        System.out.println(fileManager.getAllTasks());
        System.out.println(fileManager.getHistory());
        System.out.println("\n\n" + "new" + "\n\n");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(file);
        System.out.println(fileManager.getAllTasks());
        System.out.println(fileBackedTasksManager.getHistory());
    }
}
