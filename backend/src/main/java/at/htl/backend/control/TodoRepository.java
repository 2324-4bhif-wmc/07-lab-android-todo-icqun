package at.htl.backend.control;

import at.htl.backend.entity.Todo;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class TodoRepository {
    private ConcurrentHashMap<Integer, Todo> todos = new ConcurrentHashMap<>();

    public TodoRepository() {
    }

    public boolean addFinished(Todo todo) {
        if (todos.containsKey(todo.getId())) {
            return false;
        }

        todos.put(todo.getId(), todo);

        return true;
    }

    public List<Todo> getAll() {
        return new ArrayList<>(todos.values());
    }

    public void add(Todo todo) {
        int nextId = todos.values().stream().max(Comparator.comparingInt(Todo::getId)).get().getId() + 1;
        todo.setId(nextId);
        todos.put(nextId, todo);
    }

    public void update(int id, Todo todo) {
        todo.setId(id);
        todos.put(id, todo);
    }

    public void remove(int id) {
        todos.remove(id);
    }
}
