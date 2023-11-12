package tests;

import managers.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest {

    protected TaskManager taskManager;

    @BeforeEach
    public void makeTaskManager() {
        taskManager = createTaskManager();
        Task.resetStatics();
    }

    protected abstract TaskManager createTaskManager();


    @Test
    public void testGetAllTasks() {
        Task task = new Task("Task Title", "Task Description");
        Epic epic = new Epic("Epic", "Des");
        Subtask subtask = new Subtask("Subtask", "Des", 2);

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        Set<Object> expectedTasks = new HashSet<>(Arrays.asList(task, epic, subtask));
        Set<Object> actualTasks = new HashSet<>(taskManager.getAllTasks());

        assertEquals(expectedTasks, actualTasks);
        assertEquals(3, taskManager.getAllTasks().size());
    }

    @Test
    public void testClearAllTasks() {
        Task task = new Task("Task Title", "Task Description");
        Epic epic = new Epic("Epic", "Des");
        Subtask subtask = new Subtask("Subtask", "Des",2);

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        taskManager.clearAllTasks();

        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task("Task Title", "Task Description");
        Epic epic = new Epic("Epic", "Des");

        taskManager.addTask(task);
        taskManager.addEpic(epic);

        Task taskFromManager = taskManager.getTaskById(1);

        assertEquals(task, taskFromManager);
    }

    @Test
    public void testGetTaskByIdNonexistent() {
        Task taskFromManager = taskManager.getTaskById(999);

        assertNull(taskFromManager);
    }

    @Test
    public void testGetEpicById() {

        Epic epic = new Epic("Epic", "Des");
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description 1", Status.NEW,1);
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Description 2", Status.NEW,1);

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        epic.addSubtask(2);
        epic.addSubtask(3);

        Epic epicFromManager = taskManager.getEpicById(1);

        assertEquals(epic, epicFromManager);

        Subtask subtaskUpd1 = new Subtask(2, "Subtask 1", "Description 1", Status.NEW,1);
        Subtask subtaskUpd2 = new Subtask(3, "Subtask 2", "Description 1", Status.IN_PROGRESS,1);

        taskManager.updateSubtask(2, subtaskUpd1);
        taskManager.updateSubtask(3, subtaskUpd2);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(1).getStatus());

        Subtask subtaskUpd3 = new Subtask(2, "Subtask 1", "Description 1", Status.DONE,1);
        Subtask subtaskUpd4 = new Subtask(3, "Subtask 2", "Description 1", Status.DONE,1);

        taskManager.updateSubtask(2, subtaskUpd3);
        taskManager.updateSubtask(3, subtaskUpd4);

        assertEquals(Status.DONE, taskManager.getEpicById(1).getStatus());
    }

    @Test
    public void testGetEpicByIdNonexistent() {
        Epic epicFromManager = taskManager.getEpicById(999);
        assertNull(epicFromManager);
    }

    @Test
    public void testGetSubtaskById() {
        Epic epic = new Epic("Epic", "Des");
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description 1", Status.NEW,1);
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Description 2", Status.NEW,1);

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        assertEquals(subtask1, taskManager.getSubtaskById(2));
    }

    @Test
    public void testGetSubtaskByIdNonexistent() {
        Subtask subtaskFromManager = taskManager.getSubtaskById(999);
        assertNull(subtaskFromManager);
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Задача 1", "Описанние 1");
        taskManager.addTask(task);
        Task taskFromManager = taskManager.getTaskById(1);
        assertEquals(task, taskFromManager);
    }

    @Test
    public void testAddEpic() {
        Epic epic = new Epic("Epic", "Des");
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description 1", Status.NEW,1);
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Description 2", Status.NEW,1);

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        epic.addSubtask(2);
        epic.addSubtask(3);

        Epic epicFromManager = taskManager.getEpicById(1);

        assertEquals(epic, epicFromManager);
    }

    @Test
    public void testAddSubtask() {
        Epic epic = new Epic("Epic", "Des");
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description 1", Status.NEW,1);
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Description 2", Status.NEW,1);

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        epic.addSubtask(2);
        epic.addSubtask(3);

        assertEquals(subtask1, taskManager.getSubtaskById(2));
    }

    @Test
    public void testUpdateTask() {
        Task task1 = new Task("Name", "Description");
        Task task2 = new Task("Name2", "Description2");
        taskManager.addTask(task1);
        taskManager.updateTask(1,task2);
        assertEquals(taskManager.getTaskById(1).getTitle(), task2.getTitle());
        assertEquals(taskManager.getTaskById(1).getDescription(), task2.getDescription());
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Epic", "Des");
        epic.addSubtask(2);
        epic.addSubtask(3);

        Epic epic1 = new Epic("Epic2", "Des2");
        epic1.addSubtask(2);
        epic1.addSubtask(3);

        taskManager.addEpic(epic);
        taskManager.updateEpic(1, epic1);

        assertEquals(epic1, taskManager.getEpicById(1));
    }

    @Test
    public void testUpdateSubtask() {
        Epic epic = new Epic("Epic", "Des");
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description 1", Status.NEW,1);
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Description 2", Status.NEW,1);

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.updateSubtask(2,subtask2);

        assertEquals(subtask2, taskManager.getSubtaskById(2));
    }

    @Test
    public void testRemoveTask() {
        Task task = new Task("Task", "Des");
        taskManager.addTask(task);

        taskManager.removeTask(1);

        assertNull(taskManager.getTaskById(1));
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    public void testRemoveEpic() {
        Epic epic = new Epic("Epic", "Des");
        taskManager.addEpic(epic);
        taskManager.removeEpic(1);
        assertNull(taskManager.getEpicById(1));
        assertEquals(0,taskManager.getAllTasks().size());
    }

    @Test
    public void testRemoveSubtask() {
        Epic epic = new Epic("epic", "des");
        Subtask subtask = new Subtask("sub", "des",1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.removeSubtask(2);
        assertNull(taskManager.getSubtaskById(1));
        assertEquals(1,taskManager.getAllTasks().size());
    }

    @Test
    public void testGetSubtasksOfEpic() {
        Epic epic = new Epic("Epic", "Des");
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description 1", Status.NEW,1);
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Description 2", Status.NEW,1);

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);

        System.out.println(subtasks);
        System.out.println(Arrays.toString(taskManager.getSubtasksOfEpic(taskManager.getEpicById(1)).toArray()));

        assertArrayEquals(taskManager.getSubtasksOfEpic(taskManager.getEpicById(1)).toArray(), subtasks.toArray());
    }

    @Test
    public void testGetHistory() {
        Epic epic = new Epic("Епик", "Ек макарек");
        Subtask subtask1 = new Subtask("Саб таск 1", "опять 25", 1);
        Subtask subtask2 = new Subtask("Саб таск 2", "я в шоке", 1);
        Task task1 = new Task("Name", "Description");
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addTask(task1);
        taskManager.getEpicById(1);
        taskManager.getSubtaskById(2);
        taskManager.getTaskById(4);

        List<Task> history = List.of(epic, subtask1, task1);

        assertArrayEquals(history.toArray(), taskManager.getHistory().toArray());
    }

    @Test
    public void testGetPrioritizedTasks() {
        Epic epic = new Epic(1, "Епик", "Ек макарек",Status.NEW,60, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2,"Саб таск 1", "опять 25", Status.NEW, 30, LocalDateTime.now().plusMinutes(90),1);
        Subtask subtask2 = new Subtask(3,"Саб таск 52", "это второй", Status.NEW, 30, LocalDateTime.now().plusMinutes(60),1);
        Task task1 = new Task("Name", "Description");
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addTask(task1);

        LinkedList<Task> tasksRight = new LinkedList<>();

        tasksRight.add(epic);
        tasksRight.add(subtask2);
        tasksRight.add(subtask1);
        tasksRight.add(task1);

        assertArrayEquals(tasksRight.toArray(), taskManager.getPrioritizedTasks().toArray());
    }

    @Test
    public void testGetPrioritizedTasksWithOverlaps() {
        Epic epic = new Epic(1, "Епик", "Ек макарек",Status.NEW,60, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2,"Саб таск 1", "опять 25", Status.NEW, 30, LocalDateTime.now().plusMinutes(35),1);
        Subtask subtask2 = new Subtask(3,"Саб таск 52", "это второй", Status.NEW, 30, LocalDateTime.now().plusMinutes(60),1);
        Task task1 = new Task("Name", "Description");
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addTask(task1);

        LinkedList<Task> tasksRight = new LinkedList<>();
        tasksRight.add(epic);
        tasksRight.add(subtask2);
        tasksRight.add(task1);

        assertArrayEquals(tasksRight.toArray(), taskManager.getPrioritizedTasks().toArray());
    }

    @Test
    public void testGetPrioritizedTasksWithTwoOverlaps() {
        Epic epic = new Epic(1, "Епик", "Ек макарек",Status.NEW,60, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2,"Саб таск 1", "опять 25", Status.NEW, 30, LocalDateTime.now().plusMinutes(35),1);
        Subtask subtask2 = new Subtask(3,"Саб таск 52", "это второй", Status.NEW, 30, LocalDateTime.now().plusMinutes(40),1);
        Task task1 = new Task("Name", "Description");
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addTask(task1);

        LinkedList<Task> tasksRight = new LinkedList<>();
        tasksRight.add(epic);
        tasksRight.add(task1);

        assertArrayEquals(tasksRight.toArray(), taskManager.getPrioritizedTasks().toArray());
    }
}
