package com.app.quantitymeasurement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Sends email notifications (async).
 */
@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.username}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    // ===== Registration =====
    @Async
    public void sendRegistrationEmail(String toEmail, String userName) {
        sendEmail(toEmail,
                "Welcome to Quantity Measurement App!",
                "Hi " + userName + ",\n\nYour account has been created successfully.\n\nRegards,\nTeam");
    }

    // ===== Login =====
    @Async
    public void sendLoginNotificationEmail(String toEmail) {
        sendEmail(toEmail,
                "New login detected",
                "Hi,\n\nA new login was detected.\nIf not you, reset your password.\n\nRegards,\nTeam");
    }

    // ===== Forgot Password =====
    @Async
    public void sendForgotPasswordEmail(String toEmail) {
        sendEmail(toEmail,
                "Password changed",
                "Hi,\n\nYour password has been changed.\nIf not you, contact support.\n\nRegards,\nTeam");
    }

    // ===== Reset Password =====
    @Async
    public void sendPasswordResetEmail(String toEmail) {
        sendEmail(toEmail,
                "Password reset successful",
                "Hi,\n\nYour password has been reset successfully.\n\nRegards,\nTeam");
    }

    // ===== Common method =====
    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Email sent to {}", to);

        } catch (Exception ex) {
            log.error("Email failed for {}: {}", to, ex.getMessage());
        }
    }
}