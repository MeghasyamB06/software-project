package com.bloodlink.backend.repository;

import com.bloodlink.backend.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonorRepository
        extends JpaRepository<Donor, Integer> {

    Optional<Donor> findByUserId(Integer userId);

    List<Donor> findByIsAvailableTrue();
}