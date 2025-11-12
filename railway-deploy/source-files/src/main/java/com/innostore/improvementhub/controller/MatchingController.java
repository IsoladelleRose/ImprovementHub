package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins = "*")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/partners/{ideaId}")
    public ResponseEntity<?> matchPartners(@PathVariable Long ideaId) {
        try {
            List<Map<String, Object>> matches = matchingService.matchPartnersForIdea(ideaId);

            Map<String, Object> response = new HashMap<>();
            response.put("ideaId", ideaId);
            response.put("matches", matches);
            response.put("totalMatches", matches.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to match partners: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
