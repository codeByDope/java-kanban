package servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.FileBackedTasksManager;
import managers.Managers;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

public class HttpTaskServer {
    private final HttpServer server;
    private final static int PORT = 8080;
    private static FileBackedTasksManager manager;
    File file;

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        Epic epic = new Epic(1, "Епик", "Ек макарек", Status.NEW);
        Subtask subtask1 = new Subtask(2,"Саб таск 1", "опять 25", Status.NEW, 30, LocalDateTime.now().plusMinutes(90),1);
        Subtask subtask2 = new Subtask(3,"Саб таск 52", "это второй", Status.NEW, 30, LocalDateTime.now().plusMinutes(60),1);
        Task task = new Task("Task", "я в шоке");
        System.out.println(gson.toJson(epic));
        System.out.println(gson.toJson(subtask1));
        System.out.println(gson.toJson(subtask2));
        System.out.println(gson.toJson(task));
    }

    public HttpTaskServer() throws IOException {
        server = HttpServer.create();
        file = new File("src/Resources/FileBackedTasks.txt");
        manager = Managers.getFileManagerWithLoad(file);
        manager = FileBackedTasksManager.loadFromFile(file);

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        server.start();

        System.out.println("Сервер запущен на порту " + PORT);
    }

    static class TasksHandler implements HttpHandler {
        Gson gson = new Gson();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            String[] partsOfPath = path.split("/");

            String method = exchange.getRequestMethod();

            String id = getIdFromQuery(query);

            String response = handleRequest(partsOfPath, method, id, body);

            sendResponse(exchange, response);
        }

        private String getResourceFromPath(String[] pathParts) {
            if (pathParts.length >= 3) {
                return pathParts[2];
            }
            return "";
        }

        private String getIdFromQuery(String query) {
            if (query != null) {
                String[] queryParams = query.split("=");
                if (queryParams.length == 2 && "id".equals(queryParams[0])) {
                    return queryParams[1];
                }
            }
            return "";
        }

        private String handleRequest(String[] partsOfPath, String method, String id, String body) throws IOException {
            String resource = getResourceFromPath(partsOfPath);
            switch (method) {
                case "POST":
                    switch (resource) {
                        case "task":
                            Task task = gson.fromJson(body, Task.class);
                            if (manager.isIdInAllTasks(task.getId())) {
                                manager.updateTask(task.getId(), task);
                            } else {
                                manager.addTask(task);
                            }
                            break;
                        case "subtask":
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            if (manager.isIdInAllTasks(subtask.getId())) {
                                manager.updateSubtask(subtask.getId(), subtask);
                            } else {
                                manager.addSubtask(subtask);
                            }
                            break;
                        case "epic":
                            Epic epic = gson.fromJson(body, Epic.class);
                            if (manager.isIdInAllTasks(epic.getId())) {
                                manager.updateEpic(epic.getId(), epic);
                            } else {
                                manager.addEpic(epic);
                            }
                            break;
                    }
                    break;

                case "DELETE":
                    if (id.isEmpty()) {
                        switch (resource) {
                            case "task":
                                manager.removeAllTasks();
                                break;
                            case "subtask":
                                manager.removeAllSubtasks();
                                break;
                            case "epic":
                                manager.removeAllEpics();
                                break;
                        }
                    } else {
                        int idValue = Integer.parseInt(id);
                        switch (resource) {
                            case "task":
                                manager.removeTask(idValue);
                                break;
                            case "subtask":
                                manager.removeSubtask(idValue);
                                break;
                            case "epic":
                                manager.removeEpic(idValue);
                                break;
                        }
                    }
                    break;
                case "GET":
                    if (id.isEmpty() && !resource.isEmpty()) {
                        switch (resource) {
                            case "task":
                                return gson.toJson(manager.getTasks().toArray());
                            case "subtask":
                                return gson.toJson(manager.getSubtasks().toArray());
                            case "epic":
                                return gson.toJson(manager.getEpics().toArray());
                            case "history":
                                return gson.toJson(manager.getHistory().toArray());
                        }
                    } else if (resource.isEmpty()) {
                        return gson.toJson(manager.getPrioritizedTasks().toArray());
                    } else {
                        int idValue = Integer.parseInt(id);
                        switch (resource) {
                            case "task":
                                return gson.toJson(manager.getTaskById(idValue));
                            case "subtask":
                                if (partsOfPath.length == 4) {
                                    return gson.toJson(manager.getSubtasksOfEpic(manager.getEpicById(idValue)));
                                } else {
                                    return gson.toJson(manager.getSubtaskById(idValue));
                                }

                            case "epic":
                                return gson.toJson(manager.getEpicById(idValue));
                        }
                    }
                    break;
            }

            return "";
        }
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

}
