package servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.HttpTaskManager;
import managers.Managers;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private final HttpServer server;
    private final static int PORT = 8080;
    private static HttpTaskManager manager;

    public HttpTaskServer() throws IOException, InterruptedException {
        server = HttpServer.create();
        manager = Managers.getHttpManager(URI.create("http://localhost:8078"));

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

        private String handleRequest(String[] partsOfPath, String method, String id, String body) {
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

    public void stop() {
        server.stop(0);
    }

}
