package com.example.lab4.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Код подтверждения регистрации");
            message.setText(
                    "Ваш код подтверждения: " + code + "\n\n" +
                            "Код действителен 10 минут.\n" +
                            "Не передавайте его никому."
            );

            mailSender.send(message);

            log.info("✅ Verification code successfully sent to: {}", toEmail);

        } catch (Exception e) {
            log.error("❌ Failed to send verification code to: {}", toEmail, e);
            throw new RuntimeException("Не удалось отправить код подтверждения на почту", e);
        }
    }
}