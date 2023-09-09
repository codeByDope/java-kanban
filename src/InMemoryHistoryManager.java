import allTasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history;
    public InMemoryHistoryManager() {
        history = new LinkedList<>();
    }
    @Override
    public void add(Task task) {
        history.addFirst(task);
        if (history.size() > 10) {
            history.removeLast();
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
