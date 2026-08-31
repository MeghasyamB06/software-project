package com.bloodlink.backend.dto;

public class RegisterRequest {

    private String fullName;

    private String email;

    private String mobile;

    private String password;

    private String role;


    // =====================================================
    // GETTERS
    // =====================================================

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }


    // =====================================================
    // SETTERS
    // =====================================================

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}