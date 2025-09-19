package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.dto.PartnerRegistrationRequest;
import com.innostore.improvementhub.dto.PartnerResponse;
import com.innostore.improvementhub.entity.Partner;
import com.innostore.improvementhub.repository.PartnerRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Modern Java 21 Partner Controller
 * Uses constructor injection, records, and modern Java features
 */
@RestController
@RequestMapping("/api/partners")
@CrossOrigin(origins = "*") // Allow all origins for flexibility
public class PartnerController {

    private final PartnerRepository partnerRepository;

    // Constructor-based dependency injection (recommended over @Autowired)
    public PartnerController(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPartner(@Valid @RequestBody PartnerRegistrationRequest request) {
        try {
            // Check if email already exists
            if (partnerRepository.existsByEmail(request.email())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already exists", "field", "email"));
            }

            // Create new partner entity using modern Java 21 approach
            var partner = mapToEntity(request);

            // Save partner and map to response DTO
            var savedPartner = partnerRepository.save(partner);
            var response = mapToResponse(savedPartner);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
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