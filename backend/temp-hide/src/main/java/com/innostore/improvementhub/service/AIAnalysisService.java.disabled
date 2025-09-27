package com.innostore.improvementhub.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(AIAnalysisService.class);

    private final ChatLanguageModel chatModel;

    public AIAnalysisService(@Value("${openai.api.key:}") String openaiApiKey) {
        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            this.chatModel = OpenAiChatModel.builder()
                    .apiKey(openaiApiKey)
                    .modelName("gpt-3.5-turbo")
                    .temperature(0.7)
                    .build();
            logger.info("Initialized OpenAI chat model for AI analysis");
        } else {
            this.chatModel = null;
            logger.warn("OpenAI API key not configured - AI analysis will be unavailable");
        }
    }

    public String analyzeIdea(String coreConcept, String problemOpportunity, String email) {
        if (chatModel == null) {
            return "Error: OpenAI API key not configured. Please contact administrator.";
        }

        try {
            logger.info("Analyzing idea for email: {}", email);

            String prompt = String.format("""
                Please analyze this business idea and provide a comprehensive evaluation:

                **Core Concept:** %s

                **Problem/Opportunity:** %s

                **Submitter:** %s

                Please provide analysis covering:
                1. **Market Viability** - Is there a real market need?
                2. **Competitive Landscape** - Who are potential competitors?
                3. **Implementation Challenges** - What are the main obstacles?
                4. **Success Factors** - What would make this idea successful?
                5. **Risk Assessment** - What are the main risks?
                6. **Recommendations** - Should this idea be pursued? Why or why not?

                Format your response as a structured business analysis report.
                """, coreConcept, problemOpportunity, email);

            String analysis = chatModel.generate(prompt);
            logger.info("Generated AI analysis for idea from: {}", email);

            return analysis;

        } catch (Exception e) {
            logger.error("Error analyzing idea for {}: {}", email, e.getMessage(), e);
            return "Error analyzing idea: " + e.getMessage();
        }
    }

    public boolean isAvailable() {
        return chatModel != null;
    }
}