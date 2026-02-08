package com.example.lab3.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private long idCounter = 1;

    // 201 Created
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        user.id = idCounter++;
        users.add(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // 200 OK (по умолчанию)
    @GetMapping
    public List<User> getAll() {
        return users;
    }

    // 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return users.stream()
                .filter(u -> u.id.equals(id))
                .findFirst()
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    // 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        for (User u : users) {
            if (u.id.equals(id)) {
                u.name = user.name;
                u.email = user.email;
                return ResponseEntity.ok(u);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean removed = users.removeIf(u -> u.id.equals(id));
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
