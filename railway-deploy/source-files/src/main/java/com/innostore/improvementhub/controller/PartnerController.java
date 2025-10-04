package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.dto.PartnerRegistrationRequest;
import com.innostore.improvementhub.dto.PartnerResponse;
import com.innostore.improvementhub.entity.Partner;
import com.innostore.improvementhub.entity.User;
import com.innostore.improvementhub.repository.PartnerRepository;
import com.innostore.improvementhub.repository.UserRepository;
import com.innostore.improvementhub.service.UserService;
import com.innostore.improvementhub.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Modern Java 21 Partner Controller
 * Uses constructor injection, records, and modern Java features
 */
@RestController
@RequestMapping("/api/partners")
@CrossOrigin(origins = "*") // Allow all origins for flexibility
public class PartnerController {

    private final PartnerRepository partnerRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Constructor-based dependency injection (recommended over @Autowired)
    public PartnerController(PartnerRepository partnerRepository, UserService userService,
                           UserRepository userRepository, EmailService emailService) {
        this.partnerRepository = partnerRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPartner(@Valid @RequestBody PartnerRegistrationRequest request) {
        try {
            // Check if user exists AND innovator is already true
            Optional<User> existingUser = userService.findByEmail(request.email());
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                if (user.getInnovator() != null && user.getInnovator()) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("message", "Innovator with the same email already exists"));
                }
            }

            // Check if partner email already exists in partners table
            if (partnerRepository.existsByEmail(request.email())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Partner email already exists", "field", "email"));
            }

            // User validation passed - proceed with registration
            boolean userExists = existingUser.isPresent();
            User user;

            if (userExists) {
                // Update existing user - set innovator to true
                user = existingUser.get();
                user.setInnovator(true);
                userRepository.save(user);
            } else {
                // Create new user with credentials and set innovator to true
                user = userService.createUserAccountForPartner(request.email());
            }

            // Create new partner entity using modern Java 21 approach
            var partner = mapToEntity(request);

            // Save partner and map to response DTO
            var savedPartner = partnerRepository.save(partner);
            var response = mapToResponse(savedPartner);

            // Send email based on whether user existed
            if (userExists) {
                // Existing user - send confirmation without credentials
                emailService.sendPartnerConfirmationEmail(
                    request.email(),
                    request.companyName(),
                    request.contactPerson()
                );
            } else {
                // New user - send welcome email with credentials
                emailService.sendPartnerWelcomeEmail(
                    request.email(),
                    request.companyName(),
                    request.contactPerson(),
                    user.getEmail(), // username
                    user.getPassword(), // plain password (temporarily set for email)
                    "https://collaborationhub-frontend-production.up.railway.app/login"
                );
            }

            return ResponseEntity.ok(Map.of(
                "message", userExists ?
                    "Partner registered successfully! Check your email for confirmation." :
                    "Partner registered successfully! Check your email for login credentials.",
                "partner", response
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error registering partner", "message", e.getMessage()));
        }
    }

    /**
     * Maps PartnerRegistrationRequest record to Partner entity
     * Uses Java 21 text blocks for cleaner string formatting
     */
    private Partner mapToEntity(PartnerRegistrationRequest request) {
        var partner = new Partner();
        partner.setCompanyName(request.companyName());
        partner.setVatNumber(request.vatNumber());
        partner.setContactPerson(request.contactPerson());
        partner.setStreetAddress(request.streetAddress());
        partner.setCity(request.city());
        partner.setPostalCode(request.postalCode());
        partner.setCountry(request.country());
        partner.setEmail(request.email());

        // Use Java 21 text blocks for cleaner formatting
        var interestsSkills = formatInterestsSkills(request);
        partner.setInterestsSkills(interestsSkills);

        return partner;
    }

    /**
     * Formats interests and skills using Java 21 text blocks
     */
    private String formatInterestsSkills(PartnerRegistrationRequest request) {
        return """
            Innovation Interests: %s
            Industries/Markets: %s
            Vision/Growth: %s
            Competences/Resources: %s
            """.formatted(
                request.innovationInterests() != null ? request.innovationInterests() : "Not specified",
                request.industriesMarkets() != null ? request.industriesMarkets() : "Not specified",
                request.visionGrowth() != null ? request.visionGrowth() : "Not specified",
                request.competencesResources() != null ? request.competencesResources() : "Not specified"
            ).trim();
    }

    /**
     * Maps Partner entity to PartnerResponse record
     */
    private PartnerResponse mapToResponse(Partner partner) {
        return new PartnerResponse(
            partner.getId(),
            partner.getCompanyName(),
            partner.getContactPerson(),
            partner.getEmail(),
            partner.getCity(),
            partner.getCountry(),
            partner.getCreatedAt()
        );
    }

    @GetMapping
    public ResponseEntity<List<PartnerResponse>> getAllPartners() {
        var partners = partnerRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList(); // Java 16+ toList() method

        return ResponseEntity.ok(partners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponse> getPartnerById(@PathVariable Long id) {
        return partnerRepository.findById(id)
            .map(this::mapToResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PartnerResponse>> searchPartners(@RequestParam String companyName) {
        var partners = partnerRepository.findByCompanyNameContainingIgnoreCase(companyName)
            .stream()
            .map(this::mapToResponse)
            .toList();

        return ResponseEntity.ok(partners);
    }
}