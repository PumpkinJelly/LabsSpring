package com.example.lab3.course;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final List<Course> courses = new ArrayList<>();
    private long idCounter = 1;

    // Create
    @PostMapping
    public Course create(@RequestBody Course course) {
        course.id = idCounter++;
        courses.add(course);
        return course;
    }

    // Get all
    @GetMapping
    public List<Course> getAll() {
        return courses;
    }

    // Get by id
    @GetMapping("/{id}")
    public Course getById(@PathVariable Long id) {
        return courses.stream()
                .filter(c -> c.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update
    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @RequestBody Course course) {
        Course existing = getById(id);
        if (existing == null) return null;

        existing.title = course.title;
        existing.duration = course.duration;
        return existing;
    }

    // Delete (RULE: duration <= 40)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        Course existing = getById(id);
        if (existing == null) {
            return "Course not found";
        }

        if (existing.duration > 40) {
            return "Cannot delete course with duration greater than 40 hours";
        }

        courses.removeIf(c -> c.id.equals(id));
        return "Course deleted";
    }
}
