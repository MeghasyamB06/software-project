package com.bloodlink.backend.config;

import com.bloodlink.backend.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService
    ) {
        this.userDetailsService = userDetailsService;
    }


    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }


    // =====================================================
    // CORS CONFIGURATION
    // =====================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();


        // -------------------------------------------------
        // ALLOWED ORIGINS
        // -------------------------------------------------

        configuration.setAllowedOrigins(
                Arrays.asList(

                        // Ionic / Angular browser
                        "http://localhost:8100",
                        "http://127.0.0.1:8100",

                        // Capacitor Android WebView
                        "http://localhost",
                        "https://localhost",

                        // Capacitor
                        "capacitor://localhost"
                )
        );


        // -------------------------------------------------
        // ALLOWED HTTP METHODS
        // -------------------------------------------------

        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );


        // -------------------------------------------------
        // ALLOWED HEADERS
        // -------------------------------------------------

        configuration.setAllowedHeaders(
                Arrays.asList("*")
        );


        // -------------------------------------------------
        // CREDENTIALS
        // -------------------------------------------------

        configuration.setAllowCredentials(true);


        // -------------------------------------------------
        // REGISTER CORS CONFIGURATION
        // -------------------------------------------------

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }


    // =====================================================
    // SPRING SECURITY
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // -------------------------------------------------
                // CUSTOM USER DETAILS SERVICE
                // -------------------------------------------------

                .userDetailsService(userDetailsService)


                // -------------------------------------------------
                // ENABLE CORS
                // -------------------------------------------------

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )


                // -------------------------------------------------
                // DISABLE CSRF
                // -------------------------------------------------

                .csrf(csrf ->
                        csrf.disable()
                )


                // -------------------------------------------------
                // AUTHORIZATION
                // -------------------------------------------------

                .authorizeHttpRequests(auth -> auth


                        // -----------------------------------------
                        // CORS PREFLIGHT REQUESTS
                        // -----------------------------------------

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()


                        // -----------------------------------------
                        // AUTHENTICATION
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/auth/register"
                        ).permitAll()

                        .requestMatchers(
                                "/api/auth/login"
                        ).permitAll()


                        // -----------------------------------------
                        // DONOR APIs
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/donors/**"
                        ).permitAll()


                        // -----------------------------------------
                        // HOSPITAL APIs
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/hospitals/**"
                        ).permitAll()


                        // -----------------------------------------
                        // EMERGENCY REQUEST APIs
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/emergency-requests/**"
                        ).permitAll()


                        // -----------------------------------------
                        // ROOT ENDPOINT
                        // -----------------------------------------

                        .requestMatchers(
                                "/"
                        ).permitAll()

                        .requestMatchers("/api/notifications/**").permitAll()


                        // -----------------------------------------
                        // EVERYTHING ELSE
                        // -----------------------------------------

                        .anyRequest().authenticated()
                )


                // -------------------------------------------------
                // DISABLE DEFAULT LOGIN PAGE
                // -------------------------------------------------

                .formLogin(
                        form -> form.disable()
                );


        return http.build();
    }
}