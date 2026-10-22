package com.bloodlink.backend.dto;

public class CreateEmergencyRequestRequest {

    // =====================================================
    // PATIENT ID
    // =====================================================

    private Integer patientId;


    // =====================================================
    // HOSPITAL ID
    // =====================================================

    private Integer hospitalId;


    // =====================================================
    // BLOOD GROUP
    // =====================================================

    private String bloodGroup;


    // =====================================================
    // UNITS REQUIRED
    // =====================================================

    private Integer unitsRequired;


    // =====================================================
    // EMERGENCY LEVEL
    // =====================================================

    private String emergencyLevel;


    // =====================================================
    // LOCATION
    // =====================================================

    private String location;


    // =====================================================
    // CONTACT NUMBER
    // =====================================================

    private String contactNumber;


    // =====================================================
    // REQUIRED BY
    // =====================================================

    private String requiredBy;


    // =====================================================
    // ADDITIONAL INFORMATION
    // =====================================================

    private String additionalInformation;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CreateEmergencyRequestRequest() {
    }


    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

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


    public String getRequiredBy() {
        return requiredBy;
    }

    public void setRequiredBy(String requiredBy) {
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
}