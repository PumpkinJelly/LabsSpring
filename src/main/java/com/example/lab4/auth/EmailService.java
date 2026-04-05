package com.example.lab4.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

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

            System.out.println("✅ Письмо успешно отправлено на " + toEmail);
            System.out.println("Код: " + code);

        } catch (Exception e) {
            System.err.println("❌ ОШИБКА при отправке email на " + toEmail);
            System.err.println("Тип ошибки: " + e.getClass().getSimpleName());
            System.err.println("Сообщение: " + e.getMessage());
            e.printStackTrace();   // ← пока оставь, чтобы видеть полный стек

            // Важно: пробрасываем ошибку дальше, чтобы регистрация не прошла "успешно"
            throw new RuntimeException("Не удалось отправить код подтверждения на почту", e);
        }
    }
}