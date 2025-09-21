package com.innostore.improvementhub.service;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordService {

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generate a random password
     */
    public String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }

        return password.toString();
    }

    /**
     * Hash a password using SHA-256 with salt
     */
    public String hashPassword(String password) {
        try {
            // Generate salt
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);

            // Create MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);

            // Hash the password
            byte[] hashedPassword = md.digest(password.getBytes());

            // Combine salt and hash
            byte[] saltedHash = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, saltedHash, 0, salt.length);
            System.arraycopy(hashedPassword, 0, saltedHash, salt.length, hashedPassword.length);

            // Encode to Base64
            return Base64.getEncoder().encodeToString(saltedHash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify a password against a hash
     */
    public boolean verifyPassword(String password, String hashedPassword) {
        try {
            // Decode the stored hash
            byte[] saltedHash = Base64.getDecoder().decode(hashedPassword);

            // Extract salt (first 16 bytes)
            byte[] salt = new byte[16];
            System.arraycopy(saltedHash, 0, salt, 0, 16);

            // Create MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);

            // Hash the provided password
            byte[] hashedInput = md.digest(password.getBytes());

            // Compare with stored hash (skip salt bytes)
            if (saltedHash.length - 16 != hashedInput.length) {
                return false;
            }

            for (int i = 0; i < hashedInput.length; i++) {
                if (saltedHash[16 + i] != hashedInput[i]) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate password strength
     */
    public boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}