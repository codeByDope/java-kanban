package Tests;

import Model.Epic;
import Model.Status;
import Model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private Epic epic;

    @BeforeEach
    void makeEpic() {
        epic = new Epic("Epic Title", "Epic Description");
    }

    @Test
    void testAddSubtask() {
        int subtaskId = 1;

        epic.addSubtask(subtaskId);

        assertTrue(epic.getSubtasksIds().contains(subtaskId));
    }

    @Test
    void testRemoveSubtask() {
        int subtaskId = 1;
        epic.addSubtask(subtaskId);

        epic.removeSubtask(subtaskId);

        assertFalse(epic.getSubtasksIds().contains(subtaskId));
    }

    @Test
    void testCheckAndRefreshStatusAllNew() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.NEW,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.NEW,1));

        epic.checkAndRefreshStatus(subtasks);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void testCheckAndRefreshStatusInProgress() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.NEW,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.IN_PROGRESS,1));

        epic.checkAndRefreshStatus(subtasks);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testCheckAndRefreshStatusInProgressWithOneNewAndOneDone() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.NEW,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.DONE,1));

        epic.checkAndRefreshStatus(subtasks);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testCheckAndRefreshStatusDone() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.DONE,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.DONE,1));

        epic.checkAndRefreshStatus(subtasks);

        assertEquals(Status.DONE, epic.getStatus());
    }
}
