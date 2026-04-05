package com.example.lab4.auth;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationCodeService {

    private final Random random = new Random();

    public String generateCode() {
        int code = 100000 + random.nextInt(900000); // 6-значный код
        return String.valueOf(code);
    }

    public boolean isCodeExpired(LocalDateTime expiry) {
        return expiry == null || LocalDateTime.now().isAfter(expiry);
    }
}