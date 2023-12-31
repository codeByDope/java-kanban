import com.google.gson.Gson;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import servers.HttpTaskServer;
import servers.KVServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        new KVServer().start(); // запускаем сервер для хранения данных

        HttpTaskServer server = new HttpTaskServer(); // сервер запускается при создании объекта класса HttpTaskServer(Конструктор)
        Gson gson = new Gson();
        LocalDateTime now = LocalDateTime.now();
        Epic epic = new Epic(1, "Епик", "Ек макарек", Status.NEW);
        Subtask subtask1 = new Subtask(2, "Саб таск 1", "опять 25", Status.NEW, 30, now.plusMinutes(90), 1);
        Subtask subtask2 = new Subtask(3, "Саб таск 52", "это второй", Status.NEW, 30, now.plusMinutes(60), 1);
        Task task1 = new Task(4, "Name", "Description", Status.NEW);
        System.out.println(gson.toJson(epic));
        System.out.println(gson.toJson(subtask1));
        System.out.println(gson.toJson(subtask2));
        System.out.println(gson.toJson(task1));
        //получаю таски в формате json, чтобы можно было передать их в body запроса
    }
}

