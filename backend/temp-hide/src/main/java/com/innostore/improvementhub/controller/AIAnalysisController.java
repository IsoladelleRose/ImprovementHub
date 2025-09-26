package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.service.AIAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = {"http://localhost:4200", "https://improvementhub-frontend-production.up.railway.app"})
public class AIAnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(AIAnalysisController.class);

    @Autowired
    private AIAnalysisService aiAnalysisService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        boolean available = aiAnalysisService.isAvailable();
        if (available) {
            return ResponseEntity.ok("AI Analysis service is running");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("AI Analysis service unavailable - OpenAI API key not configured");
        }
    }

    /**
     * Analyze an idea using AI
     */
    @PostMapping("/analyze-idea")
    public ResponseEntity<Map<String, Object>> analyzeIdea(@RequestBody Map<String, String> request) {
        try {
            String coreConcept = request.get("coreConcept");
            String problemOpportunity = request.get("problemOpportunity");
            String email = request.get("email");

            logger.info("Received AI analysis request from: {}", email);

            if (coreConcept == null || coreConcept.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Core concept is required"));
            }

            if (problemOpportunity == null || problemOpportunity.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Problem/opportunity description is required"));
            }

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }

            String analysis = aiAnalysisService.analyzeIdea(
                    coreConcept.trim(),
                    problemOpportunity.trim(),
                    email.trim()
            );

            logger.info("Successfully generated AI analysis for: {}", email);

            return ResponseEntity.ok(Map.of(
                    "analysis", analysis,
                    "timestamp", System.currentTimeMillis(),
                    "status", "success"
            ));

        } catch (Exception e) {
            logger.error("Failed to analyze idea", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Analysis failed: " + e.getMessage()));
        }
    }

    /**
     * Check if AI analysis is available
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        boolean available = aiAnalysisService.isAvailable();
        return ResponseEntity.ok(Map.of(
                "available", available,
                "service", "AI Analysis",
                "timestamp", System.currentTimeMillis()
        ));
    }
}