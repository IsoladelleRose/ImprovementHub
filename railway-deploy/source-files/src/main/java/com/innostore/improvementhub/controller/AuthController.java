package com.innostore.improvementhub.controller;

import com.innostore.improvementhub.entity.User;
import com.innostore.improvementhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Optional<User> userOpt = userService.authenticateUser(request.emailAddress(), request.password());

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                var response = new LoginResponse(
                    user.getId(),
                    user.getEmailAddress(),
                    user.getInventor(),
                    user.getInnovator(),
                    "Login successful"
                );
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found", "message", "Invalid email or password"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Login failed", "message", e.getMessage()));
        }
    }

    /**
     * Change password endpoint
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            boolean success = userService.changePassword(
                request.emailAddress(),
                request.currentPassword(),
                request.newPassword()
            );

            if (success) {
                return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Password change failed", "message", "Current password is incorrect"));
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid password", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Password change failed", "message", e.getMessage()));
        }
    }

    /**
     * Forgot password endpoint
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            boolean success = userService.resetPassword(request.emailAddress());

            if (success) {
                return ResponseEntity.ok(Map.of("message", "Password reset email sent successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found", "message", "No account found with this email address"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Password reset failed", "message", e.getMessage()));
        }
    }

    /**
     * Get user profile endpoint
     */
    @GetMapping("/profile/{emailAddress}")
    public ResponseEntity<?> getUserProfile(@PathVariable String emailAddress) {
        try {
            Optional<User> userOpt = userService.getUserByEmail(emailAddress);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                var response = new UserProfileResponse(
                    user.getId(),
                    user.getEmailAddress(),
                    user.getInventor(),
                    user.getInnovator(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
                );
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to get user profile", "message", e.getMessage()));
        }
    }

    // Request/Response DTOs as records (Java 14+ feature)
    public record LoginRequest(
        String emailAddress,
        String password
    ) {}

    public record LoginResponse(
        Long userId,
        String emailAddress,
        Boolean inventor,
        Boolean innovator,
        String message
    ) {}

    public record ChangePasswordRequest(
        String emailAddress,
        String currentPassword,
        String newPassword,
        String confirmNewPassword
    ) {}

    public record ForgotPasswordRequest(
        String emailAddress
    ) {}

    public record UserProfileResponse(
        Long id,
        String emailAddress,
        Boolean inventor,
        Boolean innovator,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt
    ) {}
}