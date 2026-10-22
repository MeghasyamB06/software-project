package com.bloodlink.backend.controller;

import com.bloodlink.backend.dto.DonorMatchingResponse;
import com.bloodlink.backend.entity.Donor;
import com.bloodlink.backend.entity.RequestResponse;
import com.bloodlink.backend.repository.DonorRepository;
import com.bloodlink.backend.service.DonorMatchingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorRepository donorRepository;
    private final DonorMatchingService donorMatchingService;

    public DonorController(
            DonorRepository donorRepository,
            DonorMatchingService donorMatchingService
    ) {
        this.donorRepository =
                donorRepository;

        this.donorMatchingService =
                donorMatchingService;
    }


    // =====================================================
    // GET DONOR BY USER ID
    // =====================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<Donor> getDonorByUserId(
            @PathVariable Integer userId
    ) {

        return donorRepository
                .findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // =====================================================
    // UPDATE DONOR AVAILABILITY
    // =====================================================

    @PutMapping("/{donorId}/availability")
    public ResponseEntity<Donor> updateAvailability(
            @PathVariable Integer donorId,
            @RequestBody AvailabilityRequest request
    ) {

        return donorRepository
                .findById(donorId)
                .map(donor -> {

                    donor.setIsAvailable(
                            request.getAvailability()
                    );

                    return ResponseEntity.ok(
                            donorRepository.save(donor)
                    );
                })
                .orElseGet(
                        () -> ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // =====================================================
    // GET MATCHING REQUESTS FOR DONOR
    // =====================================================

    @GetMapping("/{donorId}/matching-requests")
    public ResponseEntity<List<DonorMatchingResponse>>
    getMatchingRequests(
            @PathVariable Integer donorId
    ) {

        if (!donorRepository.existsById(
                donorId
        )) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        List<DonorMatchingResponse> matches =
                donorMatchingService
                        .getMatchingRequestsForDonor(
                                donorId
                        );


        return ResponseEntity.ok(
                matches
        );
    }


    // =====================================================
    // ACCEPT / DECLINE MATCHED REQUEST
    // =====================================================

    @PutMapping("/{donorId}/requests/{requestId}/respond")
    public ResponseEntity<?> respondToRequest(
            @PathVariable Integer donorId,
            @PathVariable Integer requestId,
            @RequestBody ResponseRequest request
    ) {

        try {

            RequestResponse response =
                    donorMatchingService
                            .respondToRequest(
                                    donorId,
                                    requestId,
                                    request.getStatus()
                            );


            return ResponseEntity.ok(
                    response
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            e.getMessage()
                    );

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(409)
                    .body(
                            e.getMessage()
                    );

        }  catch (RuntimeException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        e.getMessage()
                );
    }
    }


    // =====================================================
    // AVAILABILITY REQUEST
    // =====================================================

    public static class AvailabilityRequest {

        private Boolean availability;


        public Boolean getAvailability() {

            return availability;
        }


        public void setAvailability(
                Boolean availability
        ) {

            this.availability =
                    availability;
        }
    }


    // =====================================================
    // RESPONSE REQUEST
    // =====================================================

    public static class ResponseRequest {

        private String status;


        public String getStatus() {

            return status;
        }


        public void setStatus(
                String status
        ) {

            this.status =
                    status;
        }
    }
}