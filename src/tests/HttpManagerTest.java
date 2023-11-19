package tests;

import managers.HttpTaskManager;
import managers.Managers;
import managers.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class HttpManagerTest extends TaskManagerTest {
    private static HttpTaskManager manager;
    private static KVServer kvServer;

    @Override
    @BeforeEach
    public void makeTaskManager() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        super.makeTaskManager();
        manager = (HttpTaskManager) taskManager;
    }

    @Override
    protected TaskManager createTaskManager() {
        try {
            return Managers.getHttpManager(new URI("http://localhost:8078"));
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @AfterEach
    public void turnKVServerOff() {
        kvServer.stop();
    }

    @Test
    public void testSave() throws URISyntaxException, IOException, InterruptedException {
        Epic epic = new Epic("Епик", "Ек макарек");
        Subtask subtask1 = new Subtask("Саб таск 1", "опять 25", 1);
        Task task = new Task("Task", "я в шоке");

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addTask(task);

        manager.getTaskById(3);
        manager.getSubtaskById(2);

        HttpTaskManager manager2 = HttpTaskManager.loadFromServer(new URI("http://localhost:8078"));

        assertArrayEquals(manager.getAllTasks().toArray(), manager2.getAllTasks().toArray());
        assertArrayEquals(manager.getHistory().toArray(), manager2.getHistory().toArray());
    }
}
