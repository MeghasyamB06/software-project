package com.bloodlink.backend.controller;

import com.bloodlink.backend.dto.LoginResponse;
import com.bloodlink.backend.dto.RegisterRequest;
import com.bloodlink.backend.entity.User;
import com.bloodlink.backend.repository.UserRepository;
import com.bloodlink.backend.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:8100",
        "http://localhost:8101"
})
public class AuthController {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;


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
    public LoginResponse login(
            @RequestBody LoginRequest request) {


        // -------------------------------------------------
        // FIND USER BY EMAIL
        // -------------------------------------------------

        Optional<User> user =
                userRepository.findByEmail(
                        request.getUsername()
                );


        // -------------------------------------------------
        // IF EMAIL NOT FOUND, TRY MOBILE
        // -------------------------------------------------

        if (user.isEmpty()) {

            user =
                    userRepository.findByMobile(
                            request.getUsername()
                    );
        }


        // -------------------------------------------------
        // USER NOT FOUND
        // -------------------------------------------------

        if (user.isEmpty()) {

            return new LoginResponse(
                    "Invalid credentials",
                    null,
                    null,
                    null,
                    null
            );
        }


        User loggedInUser = user.get();


        // -------------------------------------------------
        // CHECK PASSWORD
        // -------------------------------------------------

        if (!passwordEncoder.matches(
                request.getPassword(),
                loggedInUser.getPasswordHash()
        )) {

            return new LoginResponse(
                    "Invalid credentials",
                    null,
                    null,
                    null,
                    null
            );
        }


        // -------------------------------------------------
        // DEBUG INFORMATION
        // -------------------------------------------------

        System.out.println(
                "========================================"
        );

        System.out.println(
                "User logged in: " +
                        loggedInUser.getEmail()
        );

        System.out.println(
                "User name: " +
                        loggedInUser.getFullName()
        );

        System.out.println(
                "User role: " +
                        loggedInUser.getRole()
        );

        System.out.println(
                "========================================"
        );


        // -------------------------------------------------
        // RETURN USER INFORMATION
        // -------------------------------------------------

        return new LoginResponse(

                "Login successful",

                loggedInUser.getUserId(),

                loggedInUser.getEmail(),

                loggedInUser.getRole(),

                loggedInUser.getFullName()
        );
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