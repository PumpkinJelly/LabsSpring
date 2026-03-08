package com.example.lab4.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateStudentDTO {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Min(value = 16, message = "Age must be at least 16")
    @NotNull(message = "Age is required")
    private Integer age;
}