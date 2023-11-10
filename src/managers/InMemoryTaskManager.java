package managers;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final TreeSet<Task> allTasks;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        allTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime() != null ? task.getStartTime() : LocalDateTime.MAX));
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }


    @Override
    public ArrayList<Object> getAllTasks() {
        ArrayList<Object> allTasks = new ArrayList<>();

        for (Integer key : tasks.keySet()) {
            allTasks.add(tasks.get(key));
        }
        for (Integer key : epics.keySet()) {
            allTasks.add(epics.get(key));
        }
        for (Integer key : subtasks.keySet()) {
            allTasks.add(subtasks.get(key));
        }

        return allTasks;
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        allTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        } else {
            System.out.println("Ни-че-го");
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        } else {
            System.out.println("Ни-че-го");
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        } else {
            System.out.println("Ни-че-го");
        }
        return subtask;
    }


    public void addToHistoryById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        } else {
            Epic epic = epics.get(id);
            if (epic != null) {
                historyManager.add(epic);
            } else {
                Subtask subtask = subtasks.get(id);
                if (subtask != null) {
                    historyManager.add(subtask);
                } else {
                    System.out.println("Ни-че-го");
                }
            }
        }
    }

    @Override
    public void addTask(Task task) {
        try {
            tasks.put(task.getId(), task);
            allTasks.add(task);
            if (checkOverlaps()) throw new ManagerSaveException("Присутствуют пересечения");
        } catch(ManagerSaveException e) {
            tasks.remove(task);
            allTasks.remove(task);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEpic(Epic epic) {
        try {
            epics.put(epic.getId(), epic);
            allTasks.add(epic);
            if (checkOverlaps()) throw new ManagerSaveException("Присутствуют пересечения");
        } catch(ManagerSaveException e) {
            epics.remove(epic);
            allTasks.remove(epic);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        try {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
            allTasks.add(subtask);
            if (checkOverlaps()) throw new ManagerSaveException("Присутствуют пересечения");
        } catch(ManagerSaveException e) {
            subtasks.remove(subtask);
            epics.get(subtask.getEpicId()).removeSubtask(subtask.getId());
            allTasks.remove(subtask);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(int id, Task task) {
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).checkAndRefreshStatus(getSubtasksOfEpic(epics.get(subtask.getEpicId())));
    }

    @Override
    public void removeTask(int id) {
        Task removedTask = tasks.remove(id);
        if (removedTask != null) {
            allTasks.remove(removedTask);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpic(int id) {
        ArrayList<Integer> subtasksIds = epics.get(id).getSubtasksIds();
        for (int subtaskId : subtasksIds) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }

        Epic removedEpic = epics.remove(id);
        if (removedEpic != null) {
            allTasks.remove(removedEpic);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtask(int id) {
        Subtask removedSubtask = subtasks.remove(id);
        if (removedSubtask != null) {
            epics.get(removedSubtask.getEpicId()).removeSubtask(removedSubtask.getId());
            epics.get(removedSubtask.getEpicId()).checkAndRefreshStatus(getSubtasksOfEpic(epics.get(removedSubtask.getEpicId())));
            allTasks.remove(removedSubtask);
            historyManager.remove(id);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtaskOfEpic = new ArrayList<>();

        for (int subtaskId : epic.getSubtasksIds()) {
            subtaskOfEpic.add(subtasks.get(subtaskId));
        }
        return subtaskOfEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return allTasks;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public boolean checkOverlaps() {
        LinkedList<Task> tasks = new LinkedList<>(allTasks);
        tasks.removeIf(task -> task.getStartTime() == null);
        for (int i = 0; i < tasks.size() - 1; i++) {
            if (tasks.get(i).hasTimeOverlap(tasks.get(i+1))) return true;
        }
        return false;
    }
}
