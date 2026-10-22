package com.bloodlink.backend.repository;

import com.bloodlink.backend.entity.RequestResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestResponseRepository
        extends JpaRepository<RequestResponse, Integer> {

    boolean existsByRequestIdAndDonorId(
            Integer requestId,
            Integer donorId
    );

    Optional<RequestResponse> findByRequestIdAndDonorId(
            Integer requestId,
            Integer donorId
    );

    List<RequestResponse> findByDonorId(
            Integer donorId
    );

    List<RequestResponse> findByRequestId(
            Integer requestId
    );

    List<RequestResponse> findByDonorIdAndStatus(
            Integer donorId,
            String status
    );
}