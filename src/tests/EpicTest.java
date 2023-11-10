package tests;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private Epic epic;

    @BeforeEach
    void makeEpic() {
        epic = new Epic("Epic Title", "Epic Description");
    }

    @Test
    public void testAddSubtask() {
        int subtaskId = 1;

        epic.addSubtask(subtaskId);

        assertTrue(epic.getSubtasksIds().contains(subtaskId));
    }

    @Test
    public void testRemoveSubtask() {
        int subtaskId = 1;
        epic.addSubtask(subtaskId);

        epic.removeSubtask(subtaskId);

        assertFalse(epic.getSubtasksIds().contains(subtaskId));
    }

    @Test
    public void testCheckAndRefreshStatusAllNew() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.NEW,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.NEW,1));

        epic.checkAndRefreshStatus(subtasks);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void testCheckAndRefreshStatusInProgress() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.NEW,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.IN_PROGRESS,1));

        epic.checkAndRefreshStatus(subtasks);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testCheckAndRefreshStatusInProgressWithOneNewAndOneDone() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.NEW,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.DONE,1));

        epic.checkAndRefreshStatus(subtasks);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testCheckAndRefreshStatusDone() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.DONE,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.DONE,1));

        epic.checkAndRefreshStatus(subtasks);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void testEpicWithTime() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now();

        subtasks.add(new Subtask(1, "Subtask 1", "Description 1", Status.NEW,60, time,1));
        subtasks.add(new Subtask(2, "Subtask 2", "Description 2", Status.NEW,60, time.plusMinutes(60),1));

        epic.setEpicStartTime(subtasks);
        epic.setEpicDuration(subtasks);

        assertEquals(time, epic.getStartTime());
        assertEquals(120,epic.getDuration());
        assertEquals(time.plusMinutes(120), epic.getEndTime());

    }
}
