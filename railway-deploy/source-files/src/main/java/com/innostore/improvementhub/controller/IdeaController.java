package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.dto.IdeaRegistrationRequest;
import com.innostore.improvementhub.dto.IdeaRegistrationResponse;
import com.innostore.improvementhub.entity.Idea;
import com.innostore.improvementhub.entity.IdeaAnalysis;
import com.innostore.improvementhub.repository.IdeaRepository;
import com.innostore.improvementhub.repository.IdeaAnalysisRepository;
import com.innostore.improvementhub.repository.UserRepository;
import com.innostore.improvementhub.service.EmailService;
import com.innostore.improvementhub.service.UserService;
import com.innostore.improvementhub.service.RagService;
import com.innostore.improvementhub.service.PdfGenerationService;
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
    private IdeaAnalysisRepository ideaAnalysisRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RagService ragService;

    @Autowired
    private PdfGenerationService pdfGenerationService;

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
                idea.setTitle(request.getTitle());
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
                    "https://collaborationhub-frontend-production.up.railway.app/login"
                );

                return ResponseEntity.ok(new IdeaRegistrationResponse(
                    "Idea registered successfully! Check your email for login credentials.",
                    true
                ));
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log full stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error registering idea: " + e.getMessage() + " | Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "none"));
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

    @GetMapping("/by-email/{email}")
    public ResponseEntity<List<Idea>> getIdeasByEmail(@PathVariable String email) {
        List<Idea> ideas = ideaRepository.findByEmail(email);
        return ResponseEntity.ok(ideas);
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeAndSubmitIdea(@Valid @RequestBody IdeaRegistrationRequest request) {
        try {
            // Validate input
            if (request.getCoreConcept() == null || request.getCoreConcept().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Core concept is required");
            }
            if (request.getProblemOpportunity() == null || request.getProblemOpportunity().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Problem/Opportunity is required");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            // Step 1: Analyze the idea with RAG
            String analysis = ragService.analyzeIdea(
                request.getCoreConcept(),
                request.getProblemOpportunity()
            );

            // Step 2: Generate PDF with the analysis
            byte[] pdfBytes = pdfGenerationService.generateIdeaAnalysisPdf(
                request.getCoreConcept(),
                request.getProblemOpportunity(),
                analysis
            );

            // Step 3: Handle based on whether user wants help
            if (request.getWantsHelp() != null && request.getWantsHelp()) {
                // Check if user already exists
                Optional<User> existingUser = userService.findByEmail(request.getEmail());
                boolean userExists = existingUser.isPresent();
                User user;

                if (userExists) {
                    // Update existing user - set inventor to true
                    user = existingUser.get();
                    user.setInventor(true);
                    userRepository.save(user);
                } else {
                    // Create new user account and set inventor to true
                    user = userService.createUserAccount(request.getEmail());
                }

                Idea idea = new Idea();
                idea.setTitle(request.getTitle());
                idea.setCoreConcept(request.getCoreConcept());
                idea.setProblemOpportunity(request.getProblemOpportunity());
                idea.setWantsHelp(request.getWantsHelp());
                idea.setUserRole(request.getUserRole());
                idea.setEmail(request.getEmail());

                Idea savedIdea = ideaRepository.save(idea);

                // Save AI analysis for partner matching
                IdeaAnalysis ideaAnalysis = new IdeaAnalysis(savedIdea.getId(), analysis);
                ideaAnalysisRepository.save(ideaAnalysis);

                // Send email with PDF - include credentials only for new users
                if (userExists) {
                    // Existing user - send email without credentials
                    emailService.sendAnalysisEmailWithPdf(
                        request.getEmail(),
                        request.getCoreConcept(),
                        request.getProblemOpportunity(),
                        request.getUserRole(),
                        null,
                        null,
                        null,
                        pdfBytes,
                        true
                    );
                } else {
                    // New user - send email with credentials
                    emailService.sendAnalysisEmailWithPdf(
                        request.getEmail(),
                        request.getCoreConcept(),
                        request.getProblemOpportunity(),
                        request.getUserRole(),
                        user.getEmail(),
                        user.getPassword(),
                        "https://collaborationhub-frontend-production.up.railway.app/login",
                        pdfBytes,
                        true
                    );
                }

                return ResponseEntity.ok(new IdeaRegistrationResponse(
                    "Idea analyzed and registered successfully! Check your email for the analysis report" +
                    (userExists ? "." : " and login credentials."),
                    true
                ));
            } else {
                // User doesn't want help - just send analysis email with PDF
                emailService.sendAnalysisEmailWithPdf(
                    request.getEmail(),
                    request.getCoreConcept(),
                    request.getProblemOpportunity(),
                    null,
                    null,
                    null,
                    null,
                    pdfBytes,
                    false
                );

                return ResponseEntity.ok(new IdeaRegistrationResponse(
                    "Thank you for your submission! The analysis report has been sent to your email.",
                    false
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error analyzing idea: " + e.getMessage() + " | Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "none"));
        }
    }
}