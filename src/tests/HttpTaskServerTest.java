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

    @Test
    public void checkGetByIdsAndGetAllSubtasks() throws IOException, InterruptedException, URISyntaxException {
        String endpoint = "/tasks/subtask/?id=2";
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

        HttpRequest request6 = requestBuilder
                .GET()
                .uri(URI.create(URL + "/tasks/epic/?id=1"))
                .build();
        HttpResponse<String> response1 = client.send(request6, handler);

        HttpRequest request7 = requestBuilder
                .GET()
                .uri(URI.create(URL + "/tasks/task/?id=4"))
                .build();
        HttpResponse<String> response2 = client.send(request7, handler);

        HttpRequest request8 = requestBuilder
                .GET()
                .uri(URI.create(URL + "/tasks/subtask/"))
                .build();
        HttpResponse<String> response3 = client.send(request8, handler);

        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        subtaskArrayList.add(subtask1);
        subtaskArrayList.add(subtask2);
        epic.addSubtask(2);
        epic.addSubtask(3);
        epic.setEpicStartTime(subtaskArrayList);
        epic.setEpicDuration(subtaskArrayList);




        assertEquals(subtask1,gson.fromJson(response.body(),Subtask.class));
        assertEquals(epic, gson.fromJson(response1.body(),Epic.class));
        assertEquals(task1, gson.fromJson(response2.body(),Task.class));
        assertEquals(gson.toJson(subtaskArrayList.toArray()), response3.body());
    }

    @Test
    public void checkHistory() throws URISyntaxException, IOException, InterruptedException {
        String endpoint = "/tasks/history";
        LocalDateTime now = LocalDateTime.now();
        Epic epic = new Epic(1, "Епик", "Ек макарек",Status.NEW);
        Subtask subtask1 = new Subtask(2,"Саб таск 1", "опять 25", Status.NEW, 30, now.plusMinutes(90),1);
        Subtask subtask2 = new Subtask(3,"Саб таск 52", "это второй", Status.NEW, 30, now.plusMinutes(60),1);
        Task task1 = new Task(4,"Name", "Description",Status.NEW);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest postEpic = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(new URI(URL+"/tasks/epic/"))
                .build();

        HttpRequest postSubtask1 = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1)))
                .uri(new URI(URL+"/tasks/subtask/"))
                .build();

        HttpRequest postSubtask2 = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2)))
                .uri(new URI(URL+"/tasks/subtask/"))
                .build();

        HttpRequest postTask = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .uri(new URI(URL+"/tasks/task/"))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        client.send(postEpic, HttpResponse.BodyHandlers.discarding());
        client.send(postSubtask1, HttpResponse.BodyHandlers.discarding());
        client.send(postSubtask2, HttpResponse.BodyHandlers.discarding());
        client.send(postTask, HttpResponse.BodyHandlers.discarding());

        HttpRequest getSubtask1 = requestBuilder
                .GET()
                .uri(new URI(URL + "/tasks/subtask/?id=2"))
                .build();

        HttpRequest getSubtask2 = requestBuilder
                .GET()
                .uri(new URI(URL + "/tasks/subtask/?id=3"))
                .build();
        HttpResponse<String> responseSub1 = client.send(getSubtask1, handler);
        HttpResponse<String> responseSub2 = client.send(getSubtask2, handler);

        URI uri = URI.create(URL + endpoint);

        HttpRequest getHistory = requestBuilder
                .GET()
                .uri(uri)
                .build();

        HttpResponse<String> history = client.send(getHistory, handler);

        ArrayList<Subtask> historyRight = new ArrayList<>();
        historyRight.add(subtask1);
        historyRight.add(subtask2);

        assertEquals(gson.toJson(historyRight.toArray()), history.body());
    }

    @Test
    public void checkDelete() throws URISyntaxException, IOException, InterruptedException {
        Epic epic = new Epic(1, "Епик", "Ек макарек",Status.NEW);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest postEpic = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(new URI(URL+"/tasks/epic/"))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        client.send(postEpic, HttpResponse.BodyHandlers.discarding());

        HttpRequest deleteEpic = requestBuilder
                .DELETE()
                .uri(new URI(URL+"/tasks/epic/?id=1"))
                .build();
        client.send(deleteEpic, HttpResponse.BodyHandlers.discarding());

        HttpRequest getNonexistenceEpic = requestBuilder
                .GET()
                .uri(new URI(URL+"/tasks/epic/?id=1"))
                .build();

        HttpResponse<String> response = client.send(getNonexistenceEpic, handler);

        assertEquals(gson.toJson(null), response.body());
    }



}
