package com.app.quantitymeasurement.controller;

import lombok.extern.slf4j.Slf4j;

import com.app.quantitymeasurement.dto.request.AuthRequest;
import com.app.quantitymeasurement.dto.request.ForgotPasswordRequest;
import com.app.quantitymeasurement.dto.request.RegisterRequest;
import com.app.quantitymeasurement.dto.response.AuthResponse;
import com.app.quantitymeasurement.dto.response.MessageResponse;
import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.security.UserPrincipal;
import com.app.quantitymeasurement.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Auth APIs (JWT + OAuth2)")
public class AuthController {

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    // Register new user
    @PostMapping("/register")
    @Operation(summary = "Register user")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login user and return JWT
    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("Login: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Get current logged-in user
    @GetMapping("/me")
    @Operation(summary = "Get current user")
    public ResponseEntity<AuthResponse> getCurrentUser(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        AuthResponse response = AuthResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(response);
    }

    // Forgot password (no login required)
    @PutMapping("/forgotPassword/{email}")
    @Operation(summary = "Forgot password")
    public ResponseEntity<MessageResponse> forgotPassword(
            @PathVariable String email,
            @Valid @RequestBody ForgotPasswordRequest request) {

        log.info("Forgot password: {}", email);
        MessageResponse response = authService.forgotPassword(email, request);
        return ResponseEntity.ok(response);
    }

    // Reset password (login required)
    @PutMapping("/resetPassword/{email}")
    @Operation(summary = "Reset password")
    public ResponseEntity<MessageResponse> resetPassword(
            @PathVariable String email,

            @RequestParam
            @NotBlank(message = "Current password required")
            String currentPassword,

            @RequestParam
            @NotBlank(message = "New password required")
            @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[@#$%^&*()+\\-=])(?=.*\\d).{8,}$",
                message = "Password must be 8+ chars, 1 uppercase, 1 special, 1 digit"
            )
            String newPassword) {

        log.info("Reset password: {}", email);
        MessageResponse response = authService.resetPassword(email, currentPassword, newPassword);
        return ResponseEntity.ok(response);
    }
}