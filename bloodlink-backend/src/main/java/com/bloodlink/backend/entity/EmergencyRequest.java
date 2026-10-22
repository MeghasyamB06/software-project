package com.bloodlink.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "EmergencyRequests")
public class EmergencyRequest {

    // =====================================================
    // REQUEST ID
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;


    // =====================================================
    // PATIENT ID
    // =====================================================

    @Column(name = "patient_id")
    private Integer patientId;


    // =====================================================
    // HOSPITAL ID
    // =====================================================

    @Column(name = "hospital_id")
    private Integer hospitalId;


    // =====================================================
    // BLOOD GROUP
    // =====================================================

    @Column(
            name = "blood_group",
            nullable = false,
            length = 3
    )
    private String bloodGroup;


    // =====================================================
    // UNITS REQUIRED
    // =====================================================

    @Column(
            name = "units_required",
            nullable = false
    )
    private Integer unitsRequired;


    // =====================================================
    // EMERGENCY LEVEL
    // =====================================================

    @Column(
            name = "emergency_level",
            nullable = false,
            length = 20
    )
    private String emergencyLevel;


    // =====================================================
    // LOCATION
    // =====================================================

    @Column(
            name = "location",
            length = 255
    )
    private String location;


    // =====================================================
    // CONTACT NUMBER
    // =====================================================

    @Column(
            name = "contact_number",
            nullable = false,
            length = 15
    )
    private String contactNumber;


    // =====================================================
    // STATUS
    // =====================================================

    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private String status;


    // =====================================================
    // REQUIRED BY
    // =====================================================

    @Column(name = "required_by")
    private LocalDateTime requiredBy;


    // =====================================================
    // ADDITIONAL INFORMATION
    // =====================================================

    @Column(
            name = "additional_information",
            length = 500
    )
    private String additionalInformation;


    // =====================================================
    // CREATED AT
    // =====================================================

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;


    // =====================================================
    // UPDATED AT
    // =====================================================

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EmergencyRequest() {
    }


    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }


    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }


    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }


    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }


    public Integer getUnitsRequired() {
        return unitsRequired;
    }

    public void setUnitsRequired(Integer unitsRequired) {
        this.unitsRequired = unitsRequired;
    }


    public String getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(String emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public LocalDateTime getRequiredBy() {
        return requiredBy;
    }

    public void setRequiredBy(LocalDateTime requiredBy) {
        this.requiredBy = requiredBy;
    }


    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(
            String additionalInformation
    ) {
        this.additionalInformation =
                additionalInformation;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt
    ) {
        this.updatedAt = updatedAt;
    }
}