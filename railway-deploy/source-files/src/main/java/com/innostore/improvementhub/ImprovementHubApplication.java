package com.innostore.improvementhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ImprovementHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImprovementHubApplication.class, args);
    }
}