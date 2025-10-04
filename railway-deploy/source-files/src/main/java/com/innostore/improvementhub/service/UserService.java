package com.innostore.improvementhub.service;

import com.innostore.improvementhub.entity.User;
import com.innostore.improvementhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    public User createUserAccount(String email) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get(); // Return existing user
        }

        // Generate plain text password for email
        String plainPassword = generateRandomPassword(12);

        // Create new user
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(plainPassword)); // Hash password before saving
        user.setInnovator(true); // Set as innovator since they submitted an idea
        user.setInventor(false);

        User savedUser = userRepository.save(user);

        // Temporarily set plain password for email sending (not saved to DB)
        savedUser.setPassword(plainPassword);

        return savedUser;
    }

    public User createUserAccountForPartner(String email) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get(); // Return existing user
        }

        // Generate plain text password for email
        String plainPassword = generateRandomPassword(12);

        // Create new user
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(plainPassword)); // Hash password before saving
        user.setInnovator(true); // Set as innovator since they registered as partner
        user.setInventor(false);

        User savedUser = userRepository.save(user);

        // Temporarily set plain password for email sending (not saved to DB)
        savedUser.setPassword(plainPassword);

        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}