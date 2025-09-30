package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.dto.IdeaRegistrationRequest;
import com.innostore.improvementhub.dto.IdeaRegistrationResponse;
import com.innostore.improvementhub.entity.Idea;
import com.innostore.improvementhub.repository.IdeaRepository;
import com.innostore.improvementhub.service.EmailService;
import com.innostore.improvementhub.service.UserService;
import com.innostore.improvementhub.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ideas")
@CrossOrigin(origins = "*")
public class IdeaController {

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private com.innostore.improvementhub.service.RagService ragService;

    @PostMapping("/register")
    public ResponseEntity<?> registerIdea(@Valid @RequestBody IdeaRegistrationRequest request) {
        try {
            // Check if user wants help
            if (request.getWantsHelp() == null || !request.getWantsHelp()) {
                // User doesn't want help - only send report email, don't save to database
                emailService.sendReportEmail(
                    request.getEmail(),
                    request.getCoreConcept(),
                    request.getProblemOpportunity()
                );

                return ResponseEntity.ok(new IdeaRegistrationResponse(
                    "Thank you for your submission! A report has been sent to your email.",
                    false
                ));
            } else {
                // User wants help - create user account, save to database and send welcome email
                User user = userService.createUserAccount(request.getEmail());

                Idea idea = new Idea();
                idea.setCoreConcept(request.getCoreConcept());
                idea.setProblemOpportunity(request.getProblemOpportunity());
                idea.setWantsHelp(request.getWantsHelp());
                idea.setUserRole(request.getUserRole());
                idea.setEmail(request.getEmail());

                // Save idea
                Idea savedIdea = ideaRepository.save(idea);

                // Send welcome email with login credentials
                emailService.sendHelpRequestEmail(
                    request.getEmail(),
                    request.getCoreConcept(),
                    request.getProblemOpportunity(),
                    request.getUserRole(),
                    user.getEmail(), // username is email
                    user.getPassword(),
                    "http://localhost:4200/login"
                );

                return ResponseEntity.ok(new IdeaRegistrationResponse(
                    "Idea registered successfully! Check your email for login credentials.",
                    true
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error registering idea: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Idea>> getAllIdeas() {
        List<Idea> ideas = ideaRepository.findAll();
        return ResponseEntity.ok(ideas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Idea> getIdeaById(@PathVariable Long id) {
        Optional<Idea> idea = ideaRepository.findById(id);
        return idea.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Idea>> searchIdeas(@RequestParam String keyword) {
        List<Idea> ideas = ideaRepository.findByCoreConceptContainingIgnoreCase(keyword);
        return ResponseEntity.ok(ideas);
    }

    @GetMapping("/wants-help")
    public ResponseEntity<List<Idea>> getIdeasWantingHelp() {
        List<Idea> ideas = ideaRepository.findByWantsHelp(true);
        return ResponseEntity.ok(ideas);
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeIdea(@RequestBody IdeaRegistrationRequest request) {
        try {
            // Validate input
            if (request.getCoreConcept() == null || request.getCoreConcept().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Core concept is required");
            }
            if (request.getProblemOpportunity() == null || request.getProblemOpportunity().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Problem/Opportunity is required");
            }

            // Call RAG service to analyze the idea
            String analysis = ragService.analyzeIdea(
                request.getCoreConcept(),
                request.getProblemOpportunity()
            );

            return ResponseEntity.ok(analysis);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error analyzing idea: " + e.getMessage());
        }
    }
}