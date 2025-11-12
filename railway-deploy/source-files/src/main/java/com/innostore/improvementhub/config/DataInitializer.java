package com.innostore.improvementhub.config;

import com.innostore.improvementhub.entity.User;
import com.innostore.improvementhub.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        try {
            String adminEmail = "admin";
            Optional<User> existingAdmin = userRepository.findByEmailAddress(adminEmail);

            if (existingAdmin.isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword("admin"); // In production, this should be hashed
                admin.setInnovator(false);
                admin.setInventor(false);
                admin.setLanguage("en");

                userRepository.save(admin);
                logger.info("Admin user created successfully with email: {}", adminEmail);
            } else {
                logger.info("Admin user already exists");
            }
        } catch (Exception e) {
            logger.error("Error creating admin user: {}", e.getMessage(), e);
        }
    }
}
