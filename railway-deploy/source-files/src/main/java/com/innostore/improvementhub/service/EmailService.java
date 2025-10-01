package com.innostore.improvementhub.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService() {
        // Debug: Check if email password is configured
        String password = System.getenv("GMAIL_PASSWORD");
        System.out.println("DEBUG: GMAIL_PASSWORD is " + (password != null && !password.isEmpty() ? "configured" : "NOT configured"));
    }

    public void sendReportEmail(String toEmail, String coreConcept, String problemOpportunity) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@innostore.com");
            message.setTo(toEmail);
            message.setSubject("Report - Your Idea Submission");
            message.setText(
                "Thank you for submitting your idea!\n\n" +
                "Core Concept: " + coreConcept + "\n\n" +
                "Problem/Opportunity: " + problemOpportunity + "\n\n" +
                "We have received your submission and will review it.\n\n" +
                "Best regards,\n" +
                "INNOSTORE Team"
            );

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendHelpRequestEmail(String toEmail, String coreConcept, String problemOpportunity, String userRole, String username, String password, String loginUrl) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@innostore.com");
            message.setTo(toEmail);
            message.setSubject("Welcome - Your INNOSTORE Account Created!");
            message.setText(
                "Welcome to INNOSTORE!\n\n" +
                "Your idea has been successfully registered and we're excited to help you develop it.\n\n" +
                "IDEA SUMMARY:\n" +
                "Core Concept: " + coreConcept + "\n\n" +
                "Problem/Opportunity: " + problemOpportunity + "\n\n" +
                "Your Role: " + (userRole != null && !userRole.trim().isEmpty() ? userRole : "We'll handle everything") + "\n\n" +
                "LOGIN CREDENTIALS:\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n" +
                "Login URL: " + loginUrl + "\n\n" +
                "Please save these credentials securely. You can use them to access your account and track the progress of your idea.\n\n" +
                "We will be in touch soon with next steps for developing your idea.\n\n" +
                "Best regards,\n" +
                "INNOSTORE Team"
            );

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendAnalysisEmailWithPdf(String toEmail, String coreConcept, String problemOpportunity,
                                         String userRole, String username, String password,
                                         String loginUrl, byte[] pdfAttachment, boolean wantsHelp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("noreply@innostore.com");
            helper.setTo(toEmail);
            helper.setSubject(wantsHelp ? "Welcome - Your INNOSTORE Account & AI Analysis" : "Your Idea Analysis Report");

            String emailBody;
            if (wantsHelp) {
                emailBody = "Welcome to INNOSTORE!\n\n" +
                    "Your idea has been successfully registered and we're excited to help you develop it.\n\n" +
                    "IDEA SUMMARY:\n" +
                    "Core Concept: " + coreConcept + "\n\n" +
                    "Problem/Opportunity: " + problemOpportunity + "\n\n" +
                    "Your Role: " + (userRole != null && !userRole.trim().isEmpty() ? userRole : "We'll handle everything") + "\n\n" +
                    "LOGIN CREDENTIALS:\n" +
                    "Username: " + username + "\n" +
                    "Password: " + password + "\n" +
                    "Login URL: " + loginUrl + "\n\n" +
                    "Please find attached a detailed AI analysis of your idea.\n\n" +
                    "We will be in touch soon with next steps for developing your idea.\n\n" +
                    "Best regards,\n" +
                    "INNOSTORE Team";
            } else {
                emailBody = "Thank you for using INNOSTORE!\n\n" +
                    "We've analyzed your idea and attached a detailed report.\n\n" +
                    "IDEA SUMMARY:\n" +
                    "Core Concept: " + coreConcept + "\n\n" +
                    "Problem/Opportunity: " + problemOpportunity + "\n\n" +
                    "Please find the AI analysis attached as a PDF.\n\n" +
                    "Best regards,\n" +
                    "INNOSTORE Team";
            }

            helper.setText(emailBody);

            // Attach PDF
            helper.addAttachment("Idea-Analysis-Report.pdf", new ByteArrayResource(pdfAttachment));

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email with PDF attachment", e);
        }
    }
}