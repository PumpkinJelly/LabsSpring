package com.example.lab4.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStudentDTO {

    @Size(min = 2, max = 50)
    private String firstName;   // nullable → partial

    @Size(min = 2, max = 50)
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @Min(value = 16, message = "Age must be at least 16")
    private Integer age;
}