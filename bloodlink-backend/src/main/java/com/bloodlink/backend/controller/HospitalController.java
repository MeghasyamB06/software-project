package com.bloodlink.backend.controller;

import com.bloodlink.backend.entity.Hospital;
import com.bloodlink.backend.repository.HospitalRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalRepository hospitalRepository;

    public HospitalController(
            HospitalRepository hospitalRepository) {

        this.hospitalRepository = hospitalRepository;
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<Hospital> getHospitalByUserId(
            @PathVariable Integer userId) {

        return hospitalRepository
                .findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }
}