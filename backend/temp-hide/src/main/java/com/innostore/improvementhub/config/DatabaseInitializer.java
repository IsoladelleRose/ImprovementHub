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
    public void run(String... args) {
        // Use a separate thread to avoid blocking application startup
        new Thread(() -> {
            try {
                // Wait a bit for the application to fully start
                Thread.sleep(5000);

                logger.info("Checking if vector extension exists...");

                // Check if vector extension already exists
                Integer extensionCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM pg_extension WHERE extname = 'vector'",
                    Integer.class
                );

                if (extensionCount != null && extensionCount > 0) {
                    logger.info("Vector extension already exists");
                } else {
                    logger.info("Attempting to create vector extension...");
                    try {
                        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector");
                        logger.info("Vector extension created successfully");
                    } catch (Exception createException) {
                        logger.warn("Could not create vector extension automatically: {}", createException.getMessage());
                        logger.info("Vector extension needs to be created manually via Railway database console:");
                        logger.info("Execute: CREATE EXTENSION IF NOT EXISTS vector;");
                    }
                }

                // Verify the extension is available
                try {
                    Integer postCreateCount = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM pg_extension WHERE extname = 'vector'",
                        Integer.class
                    );

                    if (postCreateCount != null && postCreateCount > 0) {
                        logger.info("Vector extension is ready for use");
                    } else {
                        logger.warn("Vector extension not available - RAG functionality may be limited");
                    }
                } catch (Exception e) {
                    logger.warn("Could not verify vector extension status: {}", e.getMessage());
                }

            } catch (Exception e) {
                logger.warn("Vector extension initialization failed (non-critical): {}", e.getMessage());
            }
        }).start();
    }
}