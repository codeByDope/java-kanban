package Tests;

import Managers.Managers;
import Managers.FileBackedTasksManager;
import Managers.TaskManager;
import Model.Epic;
import Model.Subtask;
import Model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileBackedTasksManagerTest extends TaskManagerTest {
    File file;
    FileBackedTasksManager fileBackedTasksManager;
    @Override
    protected TaskManager createTaskManager() {
        file = new File("src/Resources/FileBackedTasks.txt");
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return Managers.getFileManager(file);
    }

    @BeforeEach
    public void setFileManager() {
        fileBackedTasksManager = (FileBackedTasksManager) taskManager;
    }

    @Test
    public void testLoadFromEmptyFile() {
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);

        assertTrue(fileBackedTasksManager2.getHistory().isEmpty());
        assertTrue(fileBackedTasksManager2.getAllTasks().isEmpty());
    }

    @Test
    public void testSave() {
        Epic epic = new Epic("Епик", "Ек макарек");
        Subtask subtask1 = new Subtask("Саб таск 1", "опять 25", 1);
        Task task = new Task("Task", "я в шоке");

        fileBackedTasksManager.addEpic(epic);
        fileBackedTasksManager.addSubtask(subtask1);
        fileBackedTasksManager.addTask(task);

        fileBackedTasksManager.getTaskById(3);
        fileBackedTasksManager.getSubtaskById(2);
        fileBackedTasksManager.save();

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);

        assertArrayEquals(fileBackedTasksManager.getAllTasks().toArray(), fileBackedTasksManager2.getAllTasks().toArray());
        assertArrayEquals(fileBackedTasksManager.getHistory().toArray(), fileBackedTasksManager2.getHistory().toArray());
    }
}
