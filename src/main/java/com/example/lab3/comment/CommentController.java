package com.example.lab3.comment;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final List<Comment> comments = new ArrayList<>();
    private long idCounter = 1;

    // Create
    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        comment.id = idCounter++;
        comments.add(comment);
        return comment;
    }

    // Get all
    @GetMapping
    public List<Comment> getAll() {
        return comments;
    }

    // Get by id
    @GetMapping("/{id}")
    public Comment getById(@PathVariable Long id) {
        return comments.stream()
                .filter(c -> c.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update ONLY text
    @PutMapping("/{id}")
    public Comment updateText(@PathVariable Long id, @RequestBody Comment comment) {
        Comment existing = getById(id);
        if (existing == null) return null;

        existing.text = comment.text;
        return existing;
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        comments.removeIf(c -> c.id.equals(id));
    }
}
