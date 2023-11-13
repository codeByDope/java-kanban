import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        LocalDateTime now = LocalDateTime.now();
        Epic epic = new Epic(1, "Епик", "Ек макарек",Status.NEW);
        Subtask subtask1 = new Subtask(2,"Саб таск 1", "опять 25", Status.NEW, 30, now.plusMinutes(90),1);
        Subtask subtask2 = new Subtask(3,"Саб таск 52", "это второй", Status.NEW, 30, now.plusMinutes(60),1);
        Task task1 = new Task("Name", "Description");

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addTask(task1);

        System.out.println(Arrays.toString(taskManager.getPrioritizedTasks().toArray()));
    }
}
