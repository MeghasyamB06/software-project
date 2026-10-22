package com.bloodlink.backend.controller;

import com.bloodlink.backend.dto.CreateEmergencyRequestRequest;
import com.bloodlink.backend.entity.EmergencyRequest;
import com.bloodlink.backend.entity.Hospital;
import com.bloodlink.backend.repository.EmergencyRequestRepository;
import com.bloodlink.backend.repository.HospitalRepository;
import com.bloodlink.backend.service.DonorMatchingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/emergency-requests")
public class EmergencyRequestController {

    private final EmergencyRequestRepository emergencyRequestRepository;
    private final HospitalRepository hospitalRepository;
    private final DonorMatchingService donorMatchingService;

    public EmergencyRequestController(
            EmergencyRequestRepository emergencyRequestRepository,
            HospitalRepository hospitalRepository,
            DonorMatchingService donorMatchingService
    ) {
        this.emergencyRequestRepository =
                emergencyRequestRepository;

        this.hospitalRepository =
                hospitalRepository;

        this.donorMatchingService =
                donorMatchingService;
    }


    // =====================================================
    // CREATE EMERGENCY REQUEST
    // =====================================================

    @PostMapping
    public ResponseEntity<?> createEmergencyRequest(
            @RequestBody CreateEmergencyRequestRequest request
    ) {

        // -------------------------------------------------
        // VALIDATE REQUEST
        // -------------------------------------------------

        if (request.getHospitalId() == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Hospital ID is required.");
        }


        if (request.getBloodGroup() == null ||
                request.getBloodGroup().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Blood group is required.");
        }


        if (request.getUnitsRequired() == null ||
                request.getUnitsRequired() <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Units required must be greater than 0.");
        }


        if (request.getEmergencyLevel() == null ||
                request.getEmergencyLevel().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Emergency level is required.");
        }


        if (request.getContactNumber() == null ||
                request.getContactNumber().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Contact number is required.");
        }


        // -------------------------------------------------
        // VERIFY HOSPITAL EXISTS
        // -------------------------------------------------

        Hospital hospital = hospitalRepository
                .findById(request.getHospitalId())
                .orElse(null);


        if (hospital == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Hospital not found.");
        }


        // -------------------------------------------------
        // CREATE ENTITY
        // -------------------------------------------------

        EmergencyRequest emergencyRequest =
                new EmergencyRequest();


        emergencyRequest.setHospitalId(
                hospital.getHospitalId()
        );


        emergencyRequest.setPatientId(
                request.getPatientId()
        );


        emergencyRequest.setBloodGroup(
                request.getBloodGroup()
                        .trim()
                        .toUpperCase()
        );


        emergencyRequest.setUnitsRequired(
                request.getUnitsRequired()
        );


        emergencyRequest.setEmergencyLevel(
                request.getEmergencyLevel()
                        .trim()
                        .toUpperCase()
        );


        emergencyRequest.setLocation(
                request.getLocation()
        );


        emergencyRequest.setContactNumber(
                request.getContactNumber()
                        .trim()
        );


        // -------------------------------------------------
        // STATUS
        // -------------------------------------------------

        /*
         * Every newly created emergency request starts
         * as OPEN.
         *
         * The donor matching service will only process
         * OPEN requests.
         */

        emergencyRequest.setStatus("OPEN");


        // -------------------------------------------------
        // REQUIRED BY
        // -------------------------------------------------

        if (request.getRequiredBy() != null &&
                !request.getRequiredBy().trim().isEmpty()) {

            try {

                LocalDateTime requiredBy =
                        LocalDateTime.parse(
                                request.getRequiredBy().trim(),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        );

                emergencyRequest.setRequiredBy(
                        requiredBy
                );

            } catch (DateTimeParseException e) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Invalid requiredBy format. " +
                                        "Use YYYY-MM-DDTHH:mm:ss."
                        );
            }
        }


        // -------------------------------------------------
        // ADDITIONAL INFORMATION
        // -------------------------------------------------

        emergencyRequest.setAdditionalInformation(
                request.getAdditionalInformation()
        );


        // -------------------------------------------------
        // TIMESTAMPS
        // -------------------------------------------------

        LocalDateTime now =
                LocalDateTime.now();


        emergencyRequest.setCreatedAt(
                now
        );


        emergencyRequest.setUpdatedAt(
                now
        );


        // -------------------------------------------------
        // SAVE EMERGENCY REQUEST
        // -------------------------------------------------

        EmergencyRequest savedRequest =
                emergencyRequestRepository.save(
                        emergencyRequest
                );


        // -------------------------------------------------
        // DONOR MATCHING
        // -------------------------------------------------

        /*
         * The emergency request has now been successfully
         * saved.
         *
         * Next, find all currently available donors whose
         * blood group is compatible with this request.
         *
         * For every matching donor, DonorMatchingService
         * creates a RequestResponses record with:
         *
         *     status = PENDING
         *
         * distance_km = NULL for now because we do not
         * yet have GPS coordinates.
         *
         * match_score is calculated from blood compatibility.
         */

        donorMatchingService.createMatchesForRequest(
                savedRequest
        );


        // -------------------------------------------------
        // RETURN CREATED REQUEST
        // -------------------------------------------------

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedRequest);
    }


    // =====================================================
    // GET REQUESTS FOR A HOSPITAL
    // =====================================================

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<EmergencyRequest>>
    getHospitalRequests(
            @PathVariable Integer hospitalId
    ) {

        // -------------------------------------------------
        // VERIFY HOSPITAL EXISTS
        // -------------------------------------------------

        if (!hospitalRepository.existsById(hospitalId)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        // -------------------------------------------------
        // GET REQUESTS
        // -------------------------------------------------

        List<EmergencyRequest> requests =
                emergencyRequestRepository
                        .findByHospitalIdOrderByCreatedAtDesc(
                                hospitalId
                        );


        return ResponseEntity.ok(requests);
    }


    // =====================================================
    // GET ONE REQUEST
    // =====================================================

    @GetMapping("/{requestId}/hospital/{hospitalId}")
    public ResponseEntity<EmergencyRequest>
    getHospitalRequest(
            @PathVariable Integer requestId,
            @PathVariable Integer hospitalId
    ) {

        return emergencyRequestRepository
                .findByRequestIdAndHospitalId(
                        requestId,
                        hospitalId
                )
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // =====================================================
    // GET OPEN REQUESTS
    // =====================================================

    @GetMapping("/open")
    public ResponseEntity<List<EmergencyRequest>>
    getOpenRequests() {

        List<EmergencyRequest> requests =
                emergencyRequestRepository
                        .findByStatusOrderByCreatedAtDesc(
                                "OPEN"
                        );

        return ResponseEntity.ok(requests);
    }


    // =====================================================
    // GET OPEN REQUESTS BY BLOOD GROUP
    // =====================================================

    @GetMapping("/open/blood-group/{bloodGroup}")
    public ResponseEntity<List<EmergencyRequest>>
    getOpenRequestsByBloodGroup(
            @PathVariable String bloodGroup
    ) {

        List<EmergencyRequest> requests =
                emergencyRequestRepository
                        .findByBloodGroupAndStatusOrderByCreatedAtDesc(
                                bloodGroup
                                        .trim()
                                        .toUpperCase(),
                                "OPEN"
                        );

        return ResponseEntity.ok(requests);
    }
}