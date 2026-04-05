package com.example.lab4.dto;     // ← Важно! Тот же пакет, что и DTO

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateStudentDTOValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassWithValidData() {
        CreateStudentDTO dto = createValidDTO();

        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Должен проходить с корректными данными");
    }

    @Test
    void shouldFailWhenFirstNameIsBlank() {
        CreateStudentDTO dto = createValidDTO();
        dto.setFirstName("");

        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        CreateStudentDTO dto = createValidDTO();
        dto.setEmail("invalid-email");

        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenAgeIsTooYoung() {
        CreateStudentDTO dto = createValidDTO();
        dto.setAge(15);

        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    private CreateStudentDTO createValidDTO() {
        CreateStudentDTO dto = new CreateStudentDTO();
        dto.setFirstName("Игорь");
        dto.setLastName("Иванов");
        dto.setEmail("igor@example.com");
        dto.setAge(20);
        return dto;
    }
}