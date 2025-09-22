package com.innostore.improvementhub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSchemaFixConfig implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Check if users table exists and has email column constraint
            // If so, make it nullable to fix the constraint violation
            jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN email DROP NOT NULL");
            System.out.println("Successfully fixed users table email constraint");
        } catch (Exception e) {
            // Ignore if table doesn't exist or column already nullable
            System.out.println("Email constraint fix not needed or already applied: " + e.getMessage());
        }
    }
}