import clients.KVTaskClient;
import managers.HttpTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import servers.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        new KVServer().start();

        HttpTaskManager manager = new HttpTaskManager(new URI("http://localhost:8078"));

        Epic epic = new Epic("Епик", "Ек макарек");
        Subtask subtask1 = new Subtask("Саб таск 1", "опять 25", 1);
        Task task = new Task("Task", "я в шоке");

        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addTask(task);

        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getSubtaskById(2));

        System.out.println(manager.getHistory());

        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getTasks());

        HttpTaskManager manager2 = HttpTaskManager.loadFromServer(new URI("http://localhost:8078"));
    }
}
