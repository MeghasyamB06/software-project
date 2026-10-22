
package com.bloodlink.backend.dto;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String mobile;
    private String password;
    private String role;

    // Donor information
    private String dateOfBirth;
    private String gender;
    private String bloodGroup;

    // Hospital / Blood Bank information
    private String organizationName;
    private String licenseNumber;
    private String address;
    private String city;

    // Getters

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    // Setters

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

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
