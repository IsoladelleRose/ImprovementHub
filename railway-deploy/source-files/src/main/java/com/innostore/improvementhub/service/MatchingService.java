package com.innostore.improvementhub.service;

import com.innostore.improvementhub.entity.IdeaAnalysis;
import com.innostore.improvementhub.entity.Partner;
import com.innostore.improvementhub.repository.IdeaAnalysisRepository;
import com.innostore.improvementhub.repository.PartnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private static final Logger logger = LoggerFactory.getLogger(MatchingService.class);

    @Autowired
    private IdeaAnalysisRepository ideaAnalysisRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    public List<Map<String, Object>> matchPartnersForIdea(Long ideaId) {
        try {
            logger.info("Starting partner matching for idea ID: {}", ideaId);

            // Get AI analysis for the idea
            Optional<IdeaAnalysis> analysisOptional = ideaAnalysisRepository.findByIdeaId(ideaId);
            if (analysisOptional.isEmpty()) {
                logger.warn("No AI analysis found for idea ID: {}", ideaId);
                return Collections.emptyList();
            }

            IdeaAnalysis analysis = analysisOptional.get();
            String analysisText = analysis.getAnalysis();

            // Extract required skills/profiles from the analysis
            Set<String> requiredSkills = extractRequiredSkills(analysisText);
            logger.info("Extracted {} required skills: {}", requiredSkills.size(), requiredSkills);

            // Get all partners
            List<Partner> allPartners = partnerRepository.findAll();
            logger.info("Found {} partners in database", allPartners.size());

            // Match and rank partners
            List<Map<String, Object>> matches = new ArrayList<>();
            for (Partner partner : allPartners) {
                int matchScore = calculateMatchScore(requiredSkills, partner);
                if (matchScore > 0) {
                    Map<String, Object> match = new HashMap<>();
                    match.put("partner", partner);
                    match.put("matchScore", matchScore);
                    match.put("matchPercentage", calculateMatchPercentage(requiredSkills.size(), matchScore));
                    matches.add(match);
                }
            }

            // Sort by match score (highest first)
            matches.sort((m1, m2) -> Integer.compare(
                (Integer) m2.get("matchScore"),
                (Integer) m1.get("matchScore")
            ));

            logger.info("Found {} partner matches for idea ID: {}", matches.size(), ideaId);
            return matches;

        } catch (Exception e) {
            logger.error("Error matching partners for idea ID: {}", ideaId, e);
            return Collections.emptyList();
        }
    }

    private Set<String> extractRequiredSkills(String analysisText) {
        Set<String> skills = new HashSet<>();

        // Extract skills from question 5 section
        String lowerAnalysis = analysisText.toLowerCase();

        // Find the section about profiles/roles needed
        int profilesIndex = lowerAnalysis.indexOf("5.");
        if (profilesIndex == -1) {
            profilesIndex = lowerAnalysis.indexOf("what profiles");
        }
        if (profilesIndex == -1) {
            profilesIndex = lowerAnalysis.indexOf("profiles/roles");
        }

        if (profilesIndex != -1) {
            // Extract text from question 5 onwards (next 500 chars)
            String profileSection = analysisText.substring(profilesIndex,
                Math.min(analysisText.length(), profilesIndex + 800));

            // Common technical and business skills to look for
            String[] skillKeywords = {
                "developer", "designer", "marketer", "engineer", "programmer",
                "frontend", "backend", "fullstack", "java", "python", "javascript",
                "react", "angular", "vue", "node", "spring", "database",
                "ux", "ui", "product manager", "project manager", "scrum master",
                "devops", "cloud", "aws", "azure", "marketing", "sales",
                "business analyst", "data scientist", "machine learning", "ai",
                "mobile", "android", "ios", "swift", "kotlin", "flutter",
                "entrepreneur", "finance", "accounting", "legal", "operations"
            };

            for (String skill : skillKeywords) {
                if (profileSection.toLowerCase().contains(skill)) {
                    skills.add(skill);
                }
            }
        }

        // If no skills found, add some default ones
        if (skills.isEmpty()) {
            skills.add("developer");
            skills.add("business");
        }

        return skills;
    }

    private int calculateMatchScore(Set<String> requiredSkills, Partner partner) {
        int score = 0;
        String partnerSkills = partner.getInterestsSkills();

        if (partnerSkills == null || partnerSkills.trim().isEmpty()) {
            return 0;
        }

        String lowerPartnerSkills = partnerSkills.toLowerCase();

        for (String requiredSkill : requiredSkills) {
            if (lowerPartnerSkills.contains(requiredSkill.toLowerCase())) {
                score++;
            }
        }

        return score;
    }

    private int calculateMatchPercentage(int totalSkills, int matchedSkills) {
        if (totalSkills == 0) return 0;
        return (int) Math.round((double) matchedSkills / totalSkills * 100);
    }
}
