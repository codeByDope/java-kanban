package managers;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileManager(File file) {
        return new FileBackedTasksManager(file);
    }

    public static HttpTaskManager getHttpManager(URI uri) throws IOException, InterruptedException {
        return new HttpTaskManager(uri);
    }
}
