package managers;


import clients.KVTaskClient;
import com.google.gson.Gson;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.net.URI;

public class HttpTaskManager extends FileBackedTasksManager{
    private final KVTaskClient kvTaskClient;
    private final Gson gson = new Gson();
    public HttpTaskManager(URI uri) throws IOException, InterruptedException {
        super(null);
        kvTaskClient = new KVTaskClient(uri.toString());
    }


    public static HttpTaskManager loadFromServer(URI uri) throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(uri);

        String tasksJson = httpTaskManager.kvTaskClient.load("tasks");
        Task[] tasks = httpTaskManager.gson.fromJson(tasksJson, Task[].class);
        for (Task task : tasks) {
            httpTaskManager.addTask(task);
        }

        String epicsJson = httpTaskManager.kvTaskClient.load("epics");
        Epic[] epics = httpTaskManager.gson.fromJson(epicsJson, Epic[].class);
        for (Epic epic : epics) {
            httpTaskManager.addEpic(epic);
        }

        String subtasksJson = httpTaskManager.kvTaskClient.load("subtasks");
        Subtask[] subtasks = httpTaskManager.gson.fromJson(subtasksJson, Subtask[].class);
        for (Subtask subtask : subtasks) {
            httpTaskManager.addSubtask(subtask);
        }

        String historyStr = httpTaskManager.kvTaskClient.load("history");
        for (int e : CSVFormatter.historyFromString(historyStr)) {
            httpTaskManager.addToHistoryById(e);
        }

        return httpTaskManager;
    }

    @Override
    protected void save() {
        try {
            if (!getTasks().isEmpty()) {
                kvTaskClient.put("tasks", gson.toJson(getTasks().toArray()));
            }
            if (!getSubtasks().isEmpty()) {
                kvTaskClient.put("subtasks", gson.toJson(getSubtasks().toArray()));
            }
            if (!getEpics().isEmpty()) {
                kvTaskClient.put("epics", gson.toJson(getEpics().toArray()));
            }

            String history = CSVFormatter.historyToString(historyManager);
            kvTaskClient.put("history", history);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
