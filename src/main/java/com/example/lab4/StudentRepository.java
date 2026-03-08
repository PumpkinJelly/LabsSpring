package com.example.lab4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Добавь эту строку:
    boolean existsByEmail(String email);

    // Опционально: можно добавить и findByEmail, если понадобится в будущем
    // Optional<Student> findByEmail(String email);
}