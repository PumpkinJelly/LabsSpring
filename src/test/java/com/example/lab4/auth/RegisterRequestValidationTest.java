package com.example.lab4.auth;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validRequestShouldPass() {
        RegisterRequest req = createValidRequest();
        assertTrue(validator.validate(req).isEmpty());
    }

    @Test
    void shortUsernameShouldFail() {
        RegisterRequest req = createValidRequest();
        req.setUsername("ab");
        assertFalse(validator.validate(req).isEmpty());
    }

    @Test
    void invalidEmailShouldFail() {
        RegisterRequest req = createValidRequest();
        req.setEmail("bad-email");
        assertFalse(validator.validate(req).isEmpty());
    }

    private RegisterRequest createValidRequest() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("igor123");
        req.setEmail("igor@example.com");
        req.setPassword("password123");
        return req;

    }
}