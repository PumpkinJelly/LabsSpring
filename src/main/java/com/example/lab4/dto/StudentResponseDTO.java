package com.example.lab4.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
}