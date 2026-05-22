package com.example.lab4.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private EmailService emailService;
    @Autowired private VerificationCodeService verificationCodeService;

    public ResponseEntity<?> register(RegisterRequest req) {
        log.info("Registration request received for email: {}", req.getEmail());

        if (userRepository.existsByEmail(req.getEmail())) {
            log.warn("Registration failed - email already exists: {}", req.getEmail());
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(List.of("USER"));
        user.setVerified(false);

        String code = verificationCodeService.generateCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        emailService.sendVerificationCode(req.getEmail(), code);

        log.info("User successfully registered: {}. Verification code sent.", req.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "Код подтверждения отправлен на вашу почту",
                "email", req.getEmail()
        ));
    }

    public ResponseEntity<?> verifyCode(VerifyCodeRequest req) {
        log.info("Code verification attempt for email: {}", req.getEmail());

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            log.warn("Verification failed - user {} is already verified", req.getEmail());
            return ResponseEntity.badRequest().body(Map.of("error", "User already verified"));
        }

        if (verificationCodeService.isCodeExpired(user.getVerificationCodeExpiry())) {
            log.warn("Verification code has expired for email: {}", req.getEmail());
            return ResponseEntity.badRequest().body(Map.of("error", "Code has expired"));
        }

        if (!req.getCode().equals(user.getVerificationCode())) {
            log.warn("Invalid verification code entered for email: {}", req.getEmail());
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid code"));
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        log.info("User {} successfully verified and received JWT token", req.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    public ResponseEntity<?> login(LoginRequest req) {
        log.info("Login attempt for email: {}", req.getEmail());

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            log.warn("Login failed - email not verified yet: {}", req.getEmail());
            return ResponseEntity.badRequest().body(Map.of("error", "Please verify your email first"));
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            log.warn("Invalid password for email: {}", req.getEmail());
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid password"));
        }

        String token = jwtService.generateToken(user);

        log.info("User {} successfully logged in", req.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}