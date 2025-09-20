package com.innostore.improvementhub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("application", "improvement-hub-backend");
        return status;
    }

    @GetMapping("/")
    public Map<String, String> root() {
        Map<String, String> info = new HashMap<>();
        info.put("application", "ImprovementHub Backend");
        info.put("status", "Running");
        info.put("version", "1.0.0");
        return info;
    }
}