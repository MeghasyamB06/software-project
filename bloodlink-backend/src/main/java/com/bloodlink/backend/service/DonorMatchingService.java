
        package com.bloodlink.backend.service;

import com.bloodlink.backend.dto.DonorMatchingResponse;
import com.bloodlink.backend.entity.Donor;
import com.bloodlink.backend.entity.EmergencyRequest;
import com.bloodlink.backend.entity.Hospital;
import com.bloodlink.backend.entity.RequestResponse;
import com.bloodlink.backend.repository.DonorRepository;
import com.bloodlink.backend.repository.EmergencyRequestRepository;
import com.bloodlink.backend.repository.HospitalRepository;
import com.bloodlink.backend.repository.RequestResponseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DonorMatchingService {

    private final DonorRepository donorRepository;
    private final EmergencyRequestRepository emergencyRequestRepository;
    private final RequestResponseRepository requestResponseRepository;
    private final HospitalRepository hospitalRepository;
    private final NotificationService notificationService;

    public DonorMatchingService(
            DonorRepository donorRepository,
            EmergencyRequestRepository emergencyRequestRepository,
            RequestResponseRepository requestResponseRepository,
            HospitalRepository hospitalRepository,
            NotificationService notificationService
    ) {
        this.donorRepository = donorRepository;
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.requestResponseRepository = requestResponseRepository;
        this.hospitalRepository = hospitalRepository;
        this.notificationService = notificationService;
    }

    // =====================================================
    // FIND MATCHING REQUESTS FOR A DONOR
    // =====================================================

    @Transactional(readOnly = true)
    public List<EmergencyRequest> findMatchingRequestsForDonor(
            Integer donorId
    ) {

        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Donor not found: " + donorId
                        )
                );

        if (!Boolean.TRUE.equals(donor.getIsAvailable())) {
            return new ArrayList<>();
        }

        List<EmergencyRequest> openRequests =
                emergencyRequestRepository.findByStatus("OPEN");

        List<EmergencyRequest> matchingRequests =
                new ArrayList<>();

        for (EmergencyRequest request : openRequests) {

            if (!isBloodCompatible(
                    donor.getBloodGroup(),
                    request.getBloodGroup()
            )) {
                continue;
            }

            if (requestResponseRepository
                    .existsByRequestIdAndDonorId(
                            request.getRequestId(),
                            donorId
                    )) {
                continue;
            }

            matchingRequests.add(request);
        }

        return matchingRequests;
    }

    // =====================================================
    // CREATE MATCHES FOR NEW EMERGENCY REQUEST
    // =====================================================

    @Transactional
    public List<RequestResponse> createMatchesForRequest(
            EmergencyRequest request
    ) {

        if (!"OPEN".equalsIgnoreCase(request.getStatus())) {
            return new ArrayList<>();
        }

        List<Donor> availableDonors =
                donorRepository.findByIsAvailableTrue();

        List<RequestResponse> createdMatches =
                new ArrayList<>();

        for (Donor donor : availableDonors) {

            if (!isBloodCompatible(
                    donor.getBloodGroup(),
                    request.getBloodGroup()
            )) {
                continue;
            }

            if (requestResponseRepository
                    .existsByRequestIdAndDonorId(
                            request.getRequestId(),
                            donor.getDonorId()
                    )) {
                continue;
            }

            RequestResponse response = new RequestResponse();

            response.setRequestId(request.getRequestId());
            response.setDonorId(donor.getDonorId());

            // GPS distance can be added when coordinates
            // are available.
            response.setDistanceKm(null);

            response.setMatchScore(
                    calculateBloodMatchScore(
                            donor.getBloodGroup(),
                            request.getBloodGroup()
                    )
            );

            response.setStatus("PENDING");
            response.setRespondedAt(null);
            response.setCreatedAt(LocalDateTime.now());

            RequestResponse saved =
                    requestResponseRepository.save(response);

            createdMatches.add(saved);

            // Notify this donor that a compatible emergency request is available.
            // Donor.userId links the donor profile to the login account used by
            // GET /api/notifications/user/{userId}.
            if (donor.getUserId() != null) {
                notificationService.createDonorEmergencyRequestNotification(
                        donor.getUserId(),
                        request.getRequestId(),
                        request.getBloodGroup(),
                        request.getEmergencyLevel(),
                        request.getLocation()
                );
            }
        }

        return createdMatches;
    }

    // =====================================================
    // GET MATCHING REQUESTS FOR DONOR
    // =====================================================

    @Transactional(readOnly = true)
    public List<DonorMatchingResponse> getMatchingRequestsForDonor(
            Integer donorId
    ) {

        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Donor not found: " + donorId
                        )
                );

        if (!Boolean.TRUE.equals(donor.getIsAvailable())) {
            return new ArrayList<>();
        }

        List<RequestResponse> responses =
                requestResponseRepository.findByDonorId(donorId);

        List<DonorMatchingResponse> result =
                new ArrayList<>();

        for (RequestResponse response : responses) {

            EmergencyRequest request =
                    emergencyRequestRepository
                            .findById(response.getRequestId())
                            .orElse(null);

            if (request == null) {
                continue;
            }

            // Only show requests that remain open.
            if (!"OPEN".equalsIgnoreCase(request.getStatus())) {
                continue;
            }

            // Only show responses awaiting the donor's decision.
            if (!"PENDING".equalsIgnoreCase(response.getStatus())) {
                continue;
            }

            DonorMatchingResponse dto =
                    new DonorMatchingResponse();

            dto.setResponseId(response.getResponseId());
            dto.setRequestId(request.getRequestId());
            dto.setDonorId(response.getDonorId());
            dto.setBloodGroup(request.getBloodGroup());
            dto.setUnitsRequired(request.getUnitsRequired());
            dto.setEmergencyLevel(request.getEmergencyLevel());
            dto.setLocation(request.getLocation());
            dto.setContactNumber(request.getContactNumber());
            dto.setRequestStatus(request.getStatus());
            dto.setResponseStatus(response.getStatus());
            dto.setRequiredBy(request.getRequiredBy());
            dto.setDistanceKm(response.getDistanceKm());
            dto.setMatchScore(response.getMatchScore());
            dto.setCreatedAt(request.getCreatedAt());

            result.add(dto);
        }

        return result;
    }

    // =====================================================
    // RESPOND TO EMERGENCY REQUEST
    // =====================================================

    /**
     * Allows the matching donor to accept or decline
     * an emergency blood request.
     *
     * The response must still be PENDING and the request
     * must still be OPEN.
     *
     * When the donor responds, a notification is created
     * for the user account associated with the hospital.
     */
    @Transactional
    public RequestResponse respondToRequest(
            Integer donorId,
            Integer requestId,
            String newStatus
    ) {

        // 1. Validate the requested response.
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Response status is required."
            );
        }

        String status = newStatus.trim().toUpperCase();

        if (!"ACCEPTED".equals(status)
                && !"DECLINED".equals(status)) {
            throw new IllegalArgumentException(
                    "Response status must be ACCEPTED or DECLINED."
            );
        }

        // 2. Retrieve the donor.
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Donor not found: " + donorId
                        )
                );

        // 3. Retrieve this donor's response to the request.
        RequestResponse response =
                requestResponseRepository
                        .findByRequestIdAndDonorId(
                                requestId,
                                donorId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No matching response found for this donor and request."
                                )
                        );

        // 4. Prevent duplicate responses.
        if (!"PENDING".equalsIgnoreCase(response.getStatus())) {
            throw new IllegalStateException(
                    "This request has already been responded to."
            );
        }

        // 5. Retrieve the emergency request.
        EmergencyRequest request =
                emergencyRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Emergency request not found: "
                                                + requestId
                                )
                        );

        // 6. The request must still be open.
        if (!"OPEN".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException(
                    "This emergency request is no longer open."
            );
        }

        // 7. Validate the hospital associated with the request.
        if (request.getHospitalId() == null) {
            throw new IllegalStateException(
                    "The emergency request has no associated hospital."
            );
        }

        Hospital hospital = hospitalRepository
                .findById(request.getHospitalId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Hospital not found for request: "
                                        + requestId
                        )
                );

        if (hospital.getUserId() == null) {
            throw new IllegalStateException(
                    "The hospital has no associated user account."
            );
        }

        // 8. Update and save the donor's response.
        response.setStatus(status);
        response.setRespondedAt(LocalDateTime.now());

        RequestResponse savedResponse =
                requestResponseRepository.save(response);

        // 9. Create a notification for the hospital's user.
        notificationService.createDonorResponseNotification(
                hospital.getUserId(),
                request.getRequestId(),
                donor.getFullName(),
                status,
                request.getBloodGroup()
        );

        // 10. Return the saved response.
        return savedResponse;
    }

    // =====================================================
    // BLOOD GROUP COMPATIBILITY
    // =====================================================

    private boolean isBloodCompatible(
            String donorBloodGroup,
            String recipientBloodGroup
    ) {

        if (donorBloodGroup == null
                || recipientBloodGroup == null) {
            return false;
        }

        String donor = donorBloodGroup.trim().toUpperCase();
        String recipient =
                recipientBloodGroup.trim().toUpperCase();

        switch (donor) {

            case "O-":
                return true;

            case "O+":
                return recipient.equals("O+")
                        || recipient.equals("A+")
                        || recipient.equals("B+")
                        || recipient.equals("AB+");

            case "A-":
                return recipient.equals("A-")
                        || recipient.equals("A+")
                        || recipient.equals("AB-")
                        || recipient.equals("AB+");

            case "A+":
                return recipient.equals("A+")
                        || recipient.equals("AB+");

            case "B-":
                return recipient.equals("B-")
                        || recipient.equals("B+")
                        || recipient.equals("AB-")
                        || recipient.equals("AB+");

            case "B+":
                return recipient.equals("B+")
                        || recipient.equals("AB+");

            case "AB-":
                return recipient.equals("AB-")
                        || recipient.equals("AB+");

            case "AB+":
                return recipient.equals("AB+");

            default:
                return false;
        }
    }

    // =====================================================
    // MATCH SCORE
    // =====================================================

    private BigDecimal calculateBloodMatchScore(
            String donorBloodGroup,
            String recipientBloodGroup
    ) {

        if (donorBloodGroup == null
                || recipientBloodGroup == null) {
            return BigDecimal.ZERO;
        }

        String donor = donorBloodGroup.trim().toUpperCase();
        String recipient =
                recipientBloodGroup.trim().toUpperCase();

        if (!isBloodCompatible(donor, recipient)) {
            return BigDecimal.ZERO;
        }

        if (donor.equals(recipient)) {
            return BigDecimal.valueOf(100);
        }

        if ("O-".equals(donor)) {
            return BigDecimal.valueOf(90);
        }

        return BigDecimal.valueOf(80);
    }
}
