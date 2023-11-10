package tests;

import managers.InMemoryHistoryManager;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    @Test
    public void testAddAndRemove() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));

        historyManager.remove(task1.getId());

        history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    public void testRemoveNonexistentTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "Description 1");

        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());

        // Trying to remove a task that doesn't exist in the history
        historyManager.remove(999);

        // The history should remain unchanged
        history = historyManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    public void testAddNullTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        // Adding a null task should not affect the history
        historyManager.add(null);

        List<Task> history = historyManager.getHistory();

        assertEquals(0, history.size());
    }
}
