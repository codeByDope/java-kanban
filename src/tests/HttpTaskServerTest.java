package tests;

import com.google.gson.Gson;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import servers.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    private static final String URL = "http://localhost:8080";
    private HttpTaskServer server;
    private KVServer kvServer;
    Gson gson = new Gson();

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        server = new HttpTaskServer();

    }

    @AfterEach
    public void turnServersOff() {
        server.stop();
        kvServer.stop();
    }

    @Test
    public void checkGetPrioritizedTasksAndAllTypesPost() throws IOException, InterruptedException, URISyntaxException {
        String endpoint = "/tasks/";
        LocalDateTime now = LocalDateTime.now();
        Epic epic = new Epic(1, "Епик", "Ек макарек",Status.NEW);
        Subtask subtask1 = new Subtask(2,"Саб таск 1", "опять 25", Status.NEW, 30, now.plusMinutes(90),1);
        Subtask subtask2 = new Subtask(3,"Саб таск 52", "это второй", Status.NEW, 30, now.plusMinutes(60),1);
        Task task1 = new Task(4,"Name", "Description",Status.NEW);

        URI uri = URI.create(URL + endpoint);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request1 = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(new URI(URL+"/tasks/epic/"))
                .build();

        HttpRequest request2 = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1)))
                .uri(new URI(URL+"/tasks/subtask/"))
                .build();

        HttpRequest request3 = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2)))
                .uri(new URI(URL+"/tasks/subtask/"))
                .build();

        HttpRequest request4 = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .uri(new URI(URL+"/tasks/task/"))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        client.send(request1, HttpResponse.BodyHandlers.discarding());
        client.send(request2, HttpResponse.BodyHandlers.discarding());
        client.send(request3, HttpResponse.BodyHandlers.discarding());
        client.send(request4, HttpResponse.BodyHandlers.discarding());

        HttpRequest request5 = requestBuilder
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request5, handler);

        LinkedList<Task> tasksRight = new LinkedList<>();
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        subtaskArrayList.add(subtask1);
        subtaskArrayList.add(subtask2);
        epic.addSubtask(2);
        epic.addSubtask(3);
        epic.setEpicStartTime(subtaskArrayList);
        epic.setEpicDuration(subtaskArrayList);

        tasksRight.add(subtask2);
        tasksRight.add(subtask1);
        tasksRight.add(epic);
        tasksRight.add(task1);

        assertEquals(gson.toJson(tasksRight.toArray()),response.body());
    }
}
