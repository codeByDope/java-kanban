import allTasks.Epic;
import allTasks.Status;
import allTasks.Subtask;
import allTasks.Task;
public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Epic epic = new Epic("Епик", "Ек макарек");
        Subtask subtask1 = new Subtask("Саб таск 1", "опять 25", 1);
        Subtask subtask2 = new Subtask("Саб таск 2", "я в шоке", 1);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic1 = new Epic("Епик1", "Ек макарек1");
        manager.addEpic(epic1);

        Subtask subtask3 = new Subtask("Саб таск 3", "я в шоке333", 4);
        manager.addSubtask(subtask3);

        Task task1 = new Task("Name", "Description");
        Task task2 = new Task("Name2", "Description2");
        manager.addTask(task1);
        manager.addTask(task2);

        System.out.println(manager.getAllTasks());
        System.out.println();

        Subtask subtaskUpd1 = new Subtask("Саб таск 2", "я в шоке", 1);
        Subtask subtaskUpd2 = new Subtask("Саб таск 3", "я в шоке333", 4);
        subtaskUpd1.setStatus(Status.DONE);
        Task taskUpd2 = new Task("Name222", "Description2");
        taskUpd2.setStatus(Status.IN_PROGRESS);
        manager.updateTask(7, taskUpd2);
        manager.updateSubtask(3, subtaskUpd1);
        manager.updateSubtask(5, subtaskUpd2);

        System.out.println(manager.getAllTasks());
        System.out.println();

        manager.removeSubtask(2);
        manager.removeEpic(4);
        manager.removeTask(7);

        System.out.println(manager.getAllTasks());
        System.out.println();

        manager.clearAllTasks();
        System.out.println(manager.getAllTasks());
    }
}
