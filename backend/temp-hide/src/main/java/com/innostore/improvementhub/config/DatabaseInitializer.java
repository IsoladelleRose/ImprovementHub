package com.innostore.improvementhub.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("Checking if vector extension exists...");

            // Check if vector extension already exists
            Integer extensionCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pg_extension WHERE extname = 'vector'",
                Integer.class
            );

            if (extensionCount != null && extensionCount > 0) {
                logger.info("Vector extension already exists");
            } else {
                logger.info("Creating vector extension...");
                jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector");
                logger.info("Vector extension created successfully");
            }

            // Verify the extension is available
            Integer postCreateCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pg_extension WHERE extname = 'vector'",
                Integer.class
            );

            if (postCreateCount != null && postCreateCount > 0) {
                logger.info("Vector extension is ready for use");
            } else {
                logger.error("Failed to create or verify vector extension");
            }

        } catch (Exception e) {
            logger.error("Error initializing vector extension: {}", e.getMessage(), e);
            // Don't fail the application startup for this
        }
    }
}