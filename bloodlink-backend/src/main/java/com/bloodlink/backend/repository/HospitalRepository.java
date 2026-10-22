package com.bloodlink.backend.repository;

import com.bloodlink.backend.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalRepository
        extends JpaRepository<Hospital, Integer> {

    Optional<Hospital> findByUserId(Integer userId);
}