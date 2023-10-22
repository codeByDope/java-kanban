package Managers;

import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        if (task == null) return;
        remove(task.getId());
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

    private void linkLast(Task task) {
        Node node = new Node(task, last, null);

        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;

            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }

        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }

        return tasks;
    }

    public static class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Task data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

}
