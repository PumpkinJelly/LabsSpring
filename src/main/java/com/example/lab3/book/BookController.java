package com.example.lab3.book;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();
    private long idCounter = 1;

    // Create
    @PostMapping
    public Book create(@RequestBody Book book) {
        book.id = idCounter++;
        books.add(book);
        return book;
    }

    // Get all
    @GetMapping
    public List<Book> getAll() {
        return books;
    }

    // Get by id
    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        return books.stream()
                .filter(b -> b.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book book) {
        Book existing = getById(id);
        if (existing == null) return null;

        existing.title = book.title;
        existing.author = book.author;
        existing.year = book.year;
        return existing;
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        books.removeIf(b -> b.id.equals(id));
    }

    // Extra: get books by author
    @GetMapping("/author/{author}")
    public List<Book> getByAuthor(@PathVariable String author) {
        return books.stream()
                .filter(b -> b.author.equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }
}
