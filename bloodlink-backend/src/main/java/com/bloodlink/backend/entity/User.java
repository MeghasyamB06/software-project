package com.bloodlink.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
public class User {

    // =====================================================
    // USER ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;


    // =====================================================
    // FULL NAME
    // =====================================================

    @Column(name = "full_name")
    private String fullName;


    // =====================================================
    // EMAIL
    // =====================================================

    @Column(name = "email", nullable = false)
    private String email;


    // =====================================================
    // MOBILE
    // =====================================================

    @Column(name = "mobile", nullable = false)
    private String mobile;


    // =====================================================
    // PASSWORD
    // =====================================================

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;


    // =====================================================
    // ROLE
    // =====================================================

    @Column(name = "role", nullable = false)
    private String role;


    // =====================================================
    // VERIFIED
    // =====================================================

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;


    // =====================================================
    // ACTIVE
    // =====================================================

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;


    // =====================================================
    // CREATED AT
    // =====================================================

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    // =====================================================
    // UPDATED AT
    // =====================================================

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // =====================================================
    // DEFAULT CONSTRUCTOR
    // =====================================================

    public User() {
    }


    // =====================================================
    // USER ID
    // =====================================================

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    // =====================================================
    // FULL NAME
    // =====================================================

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // =====================================================
    // MOBILE
    // =====================================================

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    // =====================================================
    // PASSWORD HASH
    // =====================================================

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    // =====================================================
    // ROLE
    // =====================================================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    // =====================================================
    // VERIFIED
    // =====================================================

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }


    // =====================================================
    // ACTIVE
    // =====================================================

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    // =====================================================
    // CREATED AT
    // =====================================================

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    // =====================================================
    // UPDATED AT
    // =====================================================

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}