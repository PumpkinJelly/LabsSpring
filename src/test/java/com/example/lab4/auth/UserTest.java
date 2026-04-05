package com.example.lab4.auth;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserDefaultValues() {
        User user = new User();

        assertFalse(user.isVerified());
        assertNull(user.getVerificationCode());
        assertNull(user.getVerificationCodeExpiry());
    }

    @Test
    void testSetVerificationFields() {
        User user = new User();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        user.setVerified(true);
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiry(expiry);

        assertTrue(user.isVerified());
        assertEquals("123456", user.getVerificationCode());
        assertEquals(expiry, user.getVerificationCodeExpiry());
    }
}