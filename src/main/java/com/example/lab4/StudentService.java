package com.example.lab4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public Student create(Student student) {
        return repository.save(student);
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }

    public Student update(Long id, Student updated) {
        return repository.findById(id)
                .map(student -> {
                    student.setFirstName(updated.getFirstName());
                    student.setLastName(updated.getLastName());
                    student.setEmail(updated.getEmail());
                    student.setAge(updated.getAge());
                    return repository.save(student);
                })
                .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}