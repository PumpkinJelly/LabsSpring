package com.example.lab4.auth;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateStudentDTOValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validDTOShouldPass() {
        CreateStudentDTO dto = createValid();
        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void blankFirstNameShouldFail() {
        CreateStudentDTO dto = createValid();
        dto.setFirstName("");
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void invalidEmailShouldFail() {
        CreateStudentDTO dto = createValid();
        dto.setEmail("bad-email");
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void ageBelow16ShouldFail() {
        CreateStudentDTO dto = createValid();
        dto.setAge(15);
        assertFalse(validator.validate(dto).isEmpty());
    }

    private CreateStudentDTO createValid() {
        CreateStudentDTO dto = new CreateStudentDTO();
        dto.setFirstName("Игорь");
        dto.setLastName("Иванов");
        dto.setEmail("igor@test.com");
        dto.setAge(20);
        return dto;
    }
}