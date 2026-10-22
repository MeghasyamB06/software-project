package com.bloodlink.backend.controller;

import com.bloodlink.backend.dto.LoginResponse;
import com.bloodlink.backend.dto.RegisterRequest;
import com.bloodlink.backend.entity.User;
import com.bloodlink.backend.repository.UserRepository;
import com.bloodlink.backend.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")

public class AuthController {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;

    // Supports legacy BCrypt hashes created before DelegatingPasswordEncoder.
    private final BCryptPasswordEncoder legacyBcryptEncoder = new BCryptPasswordEncoder();


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthService authService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }


    // =====================================================
    // REGISTER
    // =====================================================

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request) {

        return authService.register(request);
    }


    // =====================================================
    // LOGIN
    // =====================================================

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null
                || request.getUsername().trim().isEmpty()
                || request.getPassword() == null || request.getPassword().isEmpty()) {
            return invalidCredentials();
        }

        String username = request.getUsername().trim();
        String password = request.getPassword();

        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty() && username.contains("@")) {
            user = userRepository.findByEmail(username.toLowerCase(java.util.Locale.ROOT));
        }

        if (user.isEmpty()) {
            user = userRepository.findByMobile(username);
        }

        if (user.isEmpty()) {
            return invalidCredentials();
        }

        User loggedInUser = user.get();
        String storedHash = loggedInUser.getPasswordHash();

        if (storedHash == null || !passwordMatches(password, storedHash)) {
            return invalidCredentials();
        }

        // Never log raw passwords or password hashes.
        System.out.println("BloodLink login successful for user ID: "
                + loggedInUser.getUserId() + ", role: " + loggedInUser.getRole());

        return new LoginResponse(
                "Login successful",
                loggedInUser.getUserId(),
                loggedInUser.getEmail(),
                loggedInUser.getRole(),
                loggedInUser.getFullName()
        );
    }

    private boolean passwordMatches(String rawPassword, String storedHash) {
        try {
            if (passwordEncoder.matches(rawPassword, storedHash)) {
                return true;
            }
        } catch (IllegalArgumentException ignored) {
            // A legacy BCrypt hash may not have the "{bcrypt}" prefix.
        }

        // Only support verified BCrypt hashes from older versions; no plaintext fallback.
        if (storedHash.startsWith("$2a$")
                || storedHash.startsWith("$2b$")
                || storedHash.startsWith("$2y$")) {
            return legacyBcryptEncoder.matches(rawPassword, storedHash);
        }
        return false;
    }

    private LoginResponse invalidCredentials() {
        return new LoginResponse("Invalid credentials", null, null, null, null);
    }


    // =====================================================
    // LOGIN REQUEST
    // =====================================================

    public static class LoginRequest {

        private String username;

        private String password;


        public String getUsername() {
            return username;
        }


        public void setUsername(String username) {
            this.username = username;
        }


        public String getPassword() {
            return password;
        }


        public void setPassword(String password) {
            this.password = password;
        }
    }
}