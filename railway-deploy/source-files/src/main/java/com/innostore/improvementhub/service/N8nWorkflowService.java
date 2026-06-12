package com.innostore.improvementhub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class N8nWorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(N8nWorkflowService.class);

    @Value("${n8n.webhook.url:}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void triggerIdeaAnalysis(String title,
                                    String coreConcept,
                                    String problemOpportunity,
                                    String targetGroup,
                                    String currentStage) {
        if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
            logger.warn("n8n webhook URL is not configured (N8N_WEBHOOK_URL); skipping workflow trigger");
            return;
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("title", title);
        payload.put("core concept", coreConcept);
        payload.put("problem/opportunity", problemOpportunity);
        payload.put("target_group", targetGroup);
        payload.put("stage", currentStage == null ? null : currentStage.toUpperCase());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            logger.info("Triggering n8n workflow for idea: {}", title);
            restTemplate.postForEntity(webhookUrl, request, String.class);
            logger.info("n8n workflow triggered successfully for idea: {}", title);
        } catch (Exception e) {
            logger.error("Failed to trigger n8n workflow for idea: {}", title, e);
        }
    }
}
