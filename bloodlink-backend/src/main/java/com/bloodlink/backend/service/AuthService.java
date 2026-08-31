package com.bloodlink.backend.service;

import com.bloodlink.backend.dto.RegisterRequest;
import com.bloodlink.backend.entity.User;
import com.bloodlink.backend.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // =====================================================
    // REGISTER
    // =====================================================

    public String register(RegisterRequest request) {

        // -------------------------------------------------
        // CHECK EMAIL
        // -------------------------------------------------

        if (userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            return "Email already registered";
        }


        // -------------------------------------------------
        // CHECK MOBILE
        // -------------------------------------------------

        if (userRepository
                .findByMobile(request.getMobile())
                .isPresent()) {

            return "Mobile number already registered";
        }


        // -------------------------------------------------
        // CREATE USER
        // -------------------------------------------------

        User user = new User();


        // -------------------------------------------------
        // SAVE FULL NAME
        // -------------------------------------------------

        user.setFullName(
                request.getFullName()
        );


        // -------------------------------------------------
        // SAVE EMAIL
        // -------------------------------------------------

        user.setEmail(
                request.getEmail()
        );


        // -------------------------------------------------
        // SAVE MOBILE
        // -------------------------------------------------

        user.setMobile(
                request.getMobile()
        );


        // -------------------------------------------------
        // HASH PASSWORD
        // -------------------------------------------------

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        // -------------------------------------------------
        // SAVE ROLE
        // -------------------------------------------------

        user.setRole(
                request.getRole()
        );


        // -------------------------------------------------
        // DEFAULT ACCOUNT STATUS
        // -------------------------------------------------

        user.setIsVerified(false);

        user.setIsActive(true);


        // -------------------------------------------------
        // TIMESTAMPS
        // -------------------------------------------------

        user.setCreatedAt(
                LocalDateTime.now()
        );

        user.setUpdatedAt(
                LocalDateTime.now()
        );


        // -------------------------------------------------
        // SAVE USER
        // -------------------------------------------------

        userRepository.save(user);


        System.out.println(
                "========================================"
        );

        System.out.println(
                "New user registered"
        );

        System.out.println(
                "Name: " +
                        user.getFullName()
        );

        System.out.println(
                "Email: " +
                        user.getEmail()
        );

        System.out.println(
                "Mobile: " +
                        user.getMobile()
        );

        System.out.println(
                "Role: " +
                        user.getRole()
        );

        System.out.println(
                "========================================"
        );


        return "Registration successful";
    }
}