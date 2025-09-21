package com.innostore.improvementhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

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

    public void sendHelpRequestEmail(String toEmail, String coreConcept, String problemOpportunity, String userRole) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@innostore.com");
            message.setTo(toEmail);
            message.setSubject("Welcome - Your Idea Registration Complete");
            message.setText(
                "Welcome to INNOSTORE!\n\n" +
                "Your idea has been successfully registered and we're excited to help you develop it.\n\n" +
                "Core Concept: " + coreConcept + "\n\n" +
                "Problem/Opportunity: " + problemOpportunity + "\n\n" +
                "Your Role: " + (userRole != null ? userRole : "We'll handle everything") + "\n\n" +
                "We will be in touch soon with next steps.\n\n" +
                "Best regards,\n" +
                "INNOSTORE Team"
            );

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}