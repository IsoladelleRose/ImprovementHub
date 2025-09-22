package com.innostore.improvementhub.service;

import com.innostore.improvementhub.entity.User;
import com.innostore.improvementhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailService emailService;

    /**
     * Create or update user for idea submission
     */
    public User createOrUpdateInventorUser(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            // User exists, just update inventor flag
            User user = existingUser.get();
            user.setInventor(true);
            return userRepository.save(user);
        } else {
            // Create new user
            String password = passwordService.generateRandomPassword();
            String hashedPassword = passwordService.hashPassword(password);

            User user = new User(email, hashedPassword);
            user.setInventor(true);
            user = userRepository.save(user);

            // Send welcome email
            emailService.sendWelcomeEmail(email, password, "Inventor");

            return user;
        }
    }

    /**
     * Create or update user for partner registration
     */
    public User createOrUpdateInnovatorUser(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            // User exists, just update innovator flag
            User user = existingUser.get();
            user.setInnovator(true);
            return userRepository.save(user);
        } else {
            // Create new user
            String password = passwordService.generateRandomPassword();
            String hashedPassword = passwordService.hashPassword(password);

            User user = new User(email, hashedPassword);
            user.setInnovator(true);
            user = userRepository.save(user);

            // Send welcome email
            emailService.sendWelcomeEmail(email, password, "Innovator");

            return user;
        }
    }

    /**
     * Authenticate user login
     */
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && passwordService.verifyPassword(password, user.get().getPassword())) {
            return user;
        }

        return Optional.empty();
    }

    /**
     * Change user password
     */
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        if (!passwordService.isPasswordValid(newPassword)) {
            throw new IllegalArgumentException("New password does not meet security requirements");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        if (!passwordService.verifyPassword(currentPassword, user.getPassword())) {
            return false;
        }

        String hashedNewPassword = passwordService.hashPassword(newPassword);
        user.setPassword(hashedNewPassword);
        userRepository.save(user);

        return true;
    }

    /**
     * Reset password (forgot password functionality)
     */
    public boolean resetPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        String newPassword = passwordService.generateRandomPassword();
        String hashedPassword = passwordService.hashPassword(newPassword);

        user.setPassword(hashedPassword);
        userRepository.save(user);

        // Send password reset email
        emailService.sendPasswordResetEmail(email, newPassword);

        return true;
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Check if user exists
     */
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}