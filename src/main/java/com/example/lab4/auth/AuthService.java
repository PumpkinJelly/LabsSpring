package com.example.lab4.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private EmailService emailService;
    @Autowired private VerificationCodeService verificationCodeService;

    // 1. Регистрация + отправка кода
    public ResponseEntity<?> register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(List.of("USER"));
        user.setVerified(false);

        // Генерируем и сохраняем код
        String code = verificationCodeService.generateCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        // Отправляем код на почту
        emailService.sendVerificationCode(req.getEmail(), code);

        return ResponseEntity.ok(Map.of(
                "message", "Код подтверждения отправлен на вашу почту",
                "email", req.getEmail()
        ));
    }

    // 2. Подтверждение кода
    public ResponseEntity<?> verifyCode(VerifyCodeRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User already verified"));
        }

        if (verificationCodeService.isCodeExpired(user.getVerificationCodeExpiry())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Code has expired"));
        }

        if (!req.getCode().equals(user.getVerificationCode())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid code"));
        }

        // Успешная верификация
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    // 3. Обычный логин (только для подтверждённых пользователей)
    public ResponseEntity<?> login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please verify your email first"));
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid password"));
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}