package com.example.lab3.student;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final List<Student> students = new ArrayList<>();
    private long idCounter = 1;

    // Create
    @PostMapping
    public Student create(@RequestBody Student student) {
        student.id = idCounter++;
        students.add(student);
        return student;
    }

    // Get all
    @GetMapping
    public List<Student> getAll() {
        return students;
    }

    // Get by id
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return students.stream()
                .filter(s -> s.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update
    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        Student existing = getById(id);
        if (existing == null) return null;

        existing.fullName = student.fullName;
        existing.course = student.course;
        return existing;
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        students.removeIf(s -> s.id.equals(id));
    }

    // Extra: get students by course
    @GetMapping("/course/{course}")
    public List<Student> getByCourse(@PathVariable int course) {
        return students.stream()
                .filter(s -> s.course == course)
                .collect(Collectors.toList());
    }
}
