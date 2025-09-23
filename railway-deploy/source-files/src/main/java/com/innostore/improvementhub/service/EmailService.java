package com.innostore.improvementhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username:noreply@improvementhub.com}")
    private String fromEmail;

    /**
     * Send welcome email with login credentials
     */
    public void sendWelcomeEmail(String emailAddress, String password, String role) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailAddress);
            message.setSubject("Welcome to ImprovementHub - Your Account is Ready!");

            String content = "Dear User,\n\n" +
                    "Welcome to ImprovementHub! Your account has been created successfully.\n\n" +
                    "Your login credentials:\n" +
                    "Email: " + emailAddress + "\n" +
                    "Password: " + password + "\n" +
                    "Role: " + role + "\n\n" +
                    "You can login at: https://collaborationhub-frontend-production.up.railway.app/login\n\n" +
                    "For security reasons, please change your password after your first login.\n\n" +
                    "Best regards,\n" +
                    "The ImprovementHub Team";

            message.setText(content);
            javaMailSender.send(message);

            System.out.println("=== EMAIL SENT SUCCESSFULLY ===");
            System.out.println("From: " + fromEmail);
            System.out.println("To: " + emailAddress);
            System.out.println("Subject: Welcome to ImprovementHub - Your Account is Ready!");

        } catch (Exception e) {
            System.err.println("Failed to send welcome email to " + emailAddress + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String emailAddress, String newPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailAddress);
            message.setSubject("ImprovementHub - Password Reset");

            String content = "Dear User,\n\n" +
                    "Your password has been reset as requested.\n\n" +
                    "Your new temporary password is: " + newPassword + "\n\n" +
                    "Please login and change your password immediately for security reasons.\n\n" +
                    "Login at: https://collaborationhub-frontend-production.up.railway.app/login\n\n" +
                    "Best regards,\n" +
                    "The ImprovementHub Team";

            message.setText(content);
            javaMailSender.send(message);

            System.out.println("=== PASSWORD RESET EMAIL SENT SUCCESSFULLY ===");
            System.out.println("From: " + fromEmail);
            System.out.println("To: " + emailAddress);

        } catch (Exception e) {
            System.err.println("Failed to send password reset email to " + emailAddress + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
