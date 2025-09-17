package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.dto.PartnerRegistrationRequest;
import com.innostore.improvementhub.entity.Partner;
import com.innostore.improvementhub.repository.PartnerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partners")
@CrossOrigin(origins = "http://localhost:4200")
public class PartnerController {

    @Autowired
    private PartnerRepository partnerRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerPartner(@Valid @RequestBody PartnerRegistrationRequest request) {
        try {
            // Check if email already exists
            if (partnerRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                    .body("Email already exists");
            }

            // Create new partner entity
            Partner partner = new Partner();
            partner.setCompanyName(request.getCompanyName());
            partner.setVatNumber(request.getVatNumber());
            partner.setContactPerson(request.getContactPerson());
            partner.setStreetAddress(request.getStreetAddress());
            partner.setCity(request.getCity());
            partner.setPostalCode(request.getPostalCode());
            partner.setCountry(request.getCountry());
            partner.setEmail(request.getEmail());

            // Combine all the partner form fields into interests_skills
            StringBuilder interestsSkills = new StringBuilder();
            if (request.getInnovationInterests() != null) {
                interestsSkills.append("Innovation Interests: ").append(request.getInnovationInterests()).append("\n");
            }
            if (request.getIndustriesMarkets() != null) {
                interestsSkills.append("Industries/Markets: ").append(request.getIndustriesMarkets()).append("\n");
            }
            if (request.getVisionGrowth() != null) {
                interestsSkills.append("Vision/Growth: ").append(request.getVisionGrowth()).append("\n");
            }
            if (request.getCompetencesResources() != null) {
                interestsSkills.append("Competences/Resources: ").append(request.getCompetencesResources());
            }

            partner.setInterestsSkills(interestsSkills.toString());

            // Save partner
            Partner savedPartner = partnerRepository.save(partner);

            return ResponseEntity.ok(savedPartner);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error registering partner: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Partner>> getAllPartners() {
        List<Partner> partners = partnerRepository.findAll();
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partner> getPartnerById(@PathVariable Long id) {
        Optional<Partner> partner = partnerRepository.findById(id);
        return partner.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Partner>> searchPartners(@RequestParam String companyName) {
        List<Partner> partners = partnerRepository.findByCompanyNameContainingIgnoreCase(companyName);
        return ResponseEntity.ok(partners);
    }
}