package tests;

import managers.Managers;
import managers.TaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest{
    @Override
    protected TaskManager createTaskManager() {
        return Managers.getDefault();
    }
}
