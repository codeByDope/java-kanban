package Tests;

import Managers.Managers;
import Managers.TaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest{
    @Override
    protected TaskManager createTaskManager() {
        return Managers.getDefault();
    }
}
