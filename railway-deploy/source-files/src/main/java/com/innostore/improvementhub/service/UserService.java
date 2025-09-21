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
    public User createOrUpdateInventorUser(String emailAddress) {
        Optional<User> existingUser = userRepository.findByEmailAddress(emailAddress);

        if (existingUser.isPresent()) {
            // User exists, just update inventor flag
            User user = existingUser.get();
            user.setInventor(true);
            return userRepository.save(user);
        } else {
            // Create new user
            String password = passwordService.generateRandomPassword();
            String hashedPassword = passwordService.hashPassword(password);

            User user = new User(emailAddress, hashedPassword);
            user.setInventor(true);
            user = userRepository.save(user);

            // Send welcome email
            emailService.sendWelcomeEmail(emailAddress, password, "Inventor");

            return user;
        }
    }

    /**
     * Create or update user for partner registration
     */
    public User createOrUpdateInnovatorUser(String emailAddress) {
        Optional<User> existingUser = userRepository.findByEmailAddress(emailAddress);

        if (existingUser.isPresent()) {
            // User exists, just update innovator flag
            User user = existingUser.get();
            user.setInnovator(true);
            return userRepository.save(user);
        } else {
            // Create new user
            String password = passwordService.generateRandomPassword();
            String hashedPassword = passwordService.hashPassword(password);

            User user = new User(emailAddress, hashedPassword);
            user.setInnovator(true);
            user = userRepository.save(user);

            // Send welcome email
            emailService.sendWelcomeEmail(emailAddress, password, "Innovator");

            return user;
        }
    }

    /**
     * Authenticate user login
     */
    public Optional<User> authenticateUser(String emailAddress, String password) {
        Optional<User> user = userRepository.findByEmailAddress(emailAddress);

        if (user.isPresent() && passwordService.verifyPassword(password, user.get().getPassword())) {
            return user;
        }

        return Optional.empty();
    }

    /**
     * Change user password
     */
    public boolean changePassword(String emailAddress, String currentPassword, String newPassword) {
        if (!passwordService.isPasswordValid(newPassword)) {
            throw new IllegalArgumentException("New password does not meet security requirements");
        }

        Optional<User> userOpt = userRepository.findByEmailAddress(emailAddress);
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
    public boolean resetPassword(String emailAddress) {
        Optional<User> userOpt = userRepository.findByEmailAddress(emailAddress);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        String newPassword = passwordService.generateRandomPassword();
        String hashedPassword = passwordService.hashPassword(newPassword);

        user.setPassword(hashedPassword);
        userRepository.save(user);

        // Send password reset email
        emailService.sendPasswordResetEmail(emailAddress, newPassword);

        return true;
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }

    /**
     * Check if user exists
     */
    public boolean userExists(String emailAddress) {
        return userRepository.existsByEmailAddress(emailAddress);
    }
}