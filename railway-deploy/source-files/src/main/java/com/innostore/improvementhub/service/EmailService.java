package com.innostore.improvementhub.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    /**
     * Send welcome email with login credentials
     */
    public void sendWelcomeEmail(String emailAddress, String password, String role) {
        // TODO: Implement actual email sending
        // For now, just log the email content
        System.out.println("=== EMAIL SENT ===");
        System.out.println("To: " + emailAddress);
        System.out.println("Subject: Welcome to ImprovementHub - Your Account is Ready!");
        System.out.println("Content:");
        System.out.println("Dear User,");
        System.out.println();
        System.out.println("Welcome to ImprovementHub! Your account has been created successfully.");
        System.out.println();
        System.out.println("Your login credentials:");
        System.out.println("Email: " + emailAddress);
        System.out.println("Password: " + password);
        System.out.println("Role: " + role);
        System.out.println();
        System.out.println("You can login at: [FRONTEND_URL]/login");
        System.out.println();
        System.out.println("For security reasons, please change your password after your first login.");
        System.out.println();
        System.out.println("Best regards,");
        System.out.println("The ImprovementHub Team");
        System.out.println("==================");
    }

    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String emailAddress, String newPassword) {
        // TODO: Implement actual email sending
        System.out.println("=== PASSWORD RESET EMAIL SENT ===");
        System.out.println("To: " + emailAddress);
        System.out.println("Subject: ImprovementHub - Password Reset");
        System.out.println("Content:");
        System.out.println("Dear User,");
        System.out.println();
        System.out.println("Your password has been reset as requested.");
        System.out.println();
        System.out.println("Your new temporary password is: " + newPassword);
        System.out.println();
        System.out.println("Please login and change your password immediately for security reasons.");
        System.out.println();
        System.out.println("Login at: [FRONTEND_URL]/login");
        System.out.println();
        System.out.println("Best regards,");
        System.out.println("The ImprovementHub Team");
        System.out.println("=====================================");
    }
}