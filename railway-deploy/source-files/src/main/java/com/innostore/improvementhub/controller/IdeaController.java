package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.dto.IdeaRegistrationRequest;
import com.innostore.improvementhub.entity.Idea;
import com.innostore.improvementhub.entity.User;
import com.innostore.improvementhub.repository.IdeaRepository;
import com.innostore.improvementhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ideas")
@CrossOrigin(origins = "http://localhost:4200")
public class IdeaController {

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerIdea(@Valid @RequestBody IdeaRegistrationRequest request) {
        try {
            // Create or update user (inventor)
            User user = userService.createOrUpdateInventorUser(request.getEmail());

            // Create new idea entity
            Idea idea = new Idea();
            idea.setCoreConcept(request.getCoreConcept());
            idea.setProblemOpportunity(request.getProblemOpportunity());
            idea.setWantsHelp(request.getWantsHelp());
            idea.setUserRole(request.getUserRole());
            idea.setEmail(request.getEmail());
            idea.setUser(user); // Link to user

            // Save idea
            Idea savedIdea = ideaRepository.save(idea);

            return ResponseEntity.ok(savedIdea);

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
}