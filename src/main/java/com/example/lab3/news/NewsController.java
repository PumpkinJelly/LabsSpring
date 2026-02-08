package com.example.lab3.news;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final List<News> newsList = new ArrayList<>();
    private long idCounter = 1;

    // Create
    @PostMapping
    public News create(@RequestBody News news) {
        news.id = idCounter++;
        newsList.add(news);
        return news;
    }

    // Get all
    @GetMapping
    public List<News> getAll() {
        return newsList;
    }

    // Get by id
    @GetMapping("/{id}")
    public News getById(@PathVariable Long id) {
        return newsList.stream()
                .filter(n -> n.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update
    @PutMapping("/{id}")
    public News update(@PathVariable Long id, @RequestBody News news) {
        News existing = getById(id);
        if (existing == null) return null;

        existing.title = news.title;
        existing.content = news.content;
        existing.published = news.published;
        return existing;
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        newsList.removeIf(n -> n.id.equals(id));
    }

    // Extra: get only published news
    @GetMapping("/published")
    public List<News> getPublished() {
        return newsList.stream()
                .filter(n -> n.published)
                .collect(Collectors.toList());
    }
}
