package com.bloodlink.backend.repository;

import com.bloodlink.backend.entity.EmergencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmergencyRequestRepository
        extends JpaRepository<EmergencyRequest, Integer> {

    // =====================================================
    // FIND REQUESTS BY HOSPITAL
    // =====================================================

    List<EmergencyRequest> findByHospitalIdOrderByCreatedAtDesc(
            Integer hospitalId
    );


    // =====================================================
    // FIND ONE REQUEST BY HOSPITAL
    // =====================================================

    Optional<EmergencyRequest> findByRequestIdAndHospitalId(
            Integer requestId,
            Integer hospitalId
    );


    // =====================================================
    // FIND OPEN REQUESTS
    // =====================================================

    List<EmergencyRequest> findByStatusOrderByCreatedAtDesc(
            String status
    );


    // =====================================================
    // FIND OPEN REQUESTS FOR A BLOOD GROUP
    // =====================================================

    List<EmergencyRequest> findByBloodGroupAndStatusOrderByCreatedAtDesc(
            String bloodGroup,
            String status
    );


    // =====================================================
    // FIND ALL OPEN REQUESTS
    // =====================================================

    List<EmergencyRequest> findByStatus(
            String status
    );

}