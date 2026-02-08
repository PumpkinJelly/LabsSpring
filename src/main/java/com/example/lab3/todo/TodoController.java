package com.example.lab3.todo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final List<Todo> todos = new ArrayList<>();
    private long idCounter = 1;

    // Create
    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        todo.id = idCounter++;
        todos.add(todo);
        return todo;
    }

    // Get all
    @GetMapping
    public List<Todo> getAll() {
        return todos;
    }

    // Get by id
    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id) {
        return todos.stream()
                .filter(t -> t.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update
    @PutMapping("/{id}")
    public Todo update(@PathVariable Long id, @RequestBody Todo todo) {
        Todo existing = getById(id);
        if (existing == null) return null;

        existing.title = todo.title;
        existing.completed = todo.completed;
        return existing;
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        todos.removeIf(t -> t.id.equals(id));
    }

    // Extra: get only completed todos
    @GetMapping("/completed")
    public List<Todo> getCompleted() {
        return todos.stream()
                .filter(t -> t.completed)
                .collect(Collectors.toList());
    }
}
