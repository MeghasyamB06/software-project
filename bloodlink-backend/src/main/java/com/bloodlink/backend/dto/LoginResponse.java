package com.bloodlink.backend.dto;

public class LoginResponse {

    private String message;

    private Integer userId;

    private String email;

    private String role;

    private String fullName;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LoginResponse(
            String message,
            Integer userId,
            String email,
            String role,
            String fullName) {

        this.message = message;
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
    }


    // =====================================================
    // GETTERS
    // =====================================================

    public String getMessage() {
        return message;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }


    // =====================================================
    // SETTERS
    // =====================================================

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}