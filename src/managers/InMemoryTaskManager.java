package managers;

import exceptions.OverlapException;
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
    protected final HistoryManager historyManager;

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
        try {
            Task task = tasks.get(id);
            if (task != null) {
                historyManager.add(task);
                return task;
            } else {
                throw new NullPointerException("Task с ID " + id + " не найден");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }


    }

    @Override
    public Epic getEpicById(int id) {
        try {
            Epic epic = epics.get(id);
            if (epic != null) {
                historyManager.add(epic);
                return epic;
            } else {
                throw new NullPointerException("Epic с ID " + id + " не найден");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        try {
            Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                historyManager.add(subtask);
                return subtask;
            } else {
                throw new NullPointerException("Subtask с ID " + id + " не найден");
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    protected void addToHistoryById(int id) {
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
                    throw new NullPointerException("Задача с ID " + id + " не найдена");
                }
            }
        }
    }

    @Override
    public void addTask(Task task) {
        try {
            if (!checkOverlaps(task)) {
                tasks.put(task.getId(), task);
                allTasks.add(task);
            } else {
                throw new OverlapException("Присутствуют пересечения!");
            }
        } catch (OverlapException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEpic(Epic epic) {
        try {
            if (!checkOverlaps(epic)) {
                epics.put(epic.getId(), epic);
                allTasks.add(epic);
            } else {
                throw new OverlapException("Присутствуют пересечения!");
            }
        } catch (OverlapException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        try {
            if (!checkOverlaps(subtask)) {
                Epic epicOfSubtask = epics.get(subtask.getEpicId());
                subtasks.put(subtask.getId(), subtask);
                if (!epicOfSubtask.getSubtasksIds().contains(subtask.getId())) {
                    epicOfSubtask.addSubtask(subtask.getId());
                }
                allTasks.add(subtask);
                if (subtask.getStartTime() != null) {
                    epicOfSubtask.setEpicStartTime(getSubtasksOfEpic(epicOfSubtask));
                    epicOfSubtask.setEpicDuration(getSubtasksOfEpic(epicOfSubtask));
                }
            } else {
                throw new OverlapException("Присутствуют пересечения!");
            }
        } catch (OverlapException e) {
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
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllTasks() {
        Collection<Task> savedTasks = tasks.values();
        for (Task task: savedTasks) {
            removeTask(task.getId());
        }
    }

    @Override
    public void removeAllSubtasks() {
        Collection<Subtask> savedSubtasks = subtasks.values();
        for (Subtask subtask: savedSubtasks) {
            removeSubtask(subtask.getId());
        }
    }

    @Override
    public void removeAllEpics() {
        Collection<Epic> savedEpics = epics.values();
        for (Epic epic: savedEpics) {
            removeEpic(epic.getId());
        }
    }

    public boolean isIdInAllTasks(int id) {
        if (tasks.containsKey(id) || subtasks.containsKey(id) || epics.containsKey(id)) {
            return true;
        }
        return false;
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return allTasks;
    }

    private boolean checkOverlaps(Task newTask) {

        LinkedList<Task> tasks = new LinkedList<>(allTasks);
        tasks.removeIf(task -> task.getStartTime() == null);
        if (newTask.getStartTime() != null) {
            for (Task existingTask : tasks) {
                if (existingTask.hasTimeOverlap(newTask)) {
                    return true;
                }
            }
        }
        return false;
    }
}
