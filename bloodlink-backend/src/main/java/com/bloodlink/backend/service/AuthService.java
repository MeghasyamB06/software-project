
package com.bloodlink.backend.service;

import com.bloodlink.backend.dto.RegisterRequest;
import com.bloodlink.backend.entity.Donor;
import com.bloodlink.backend.entity.Hospital;
import com.bloodlink.backend.entity.User;
import com.bloodlink.backend.repository.DonorRepository;
import com.bloodlink.backend.repository.HospitalRepository;
import com.bloodlink.backend.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final DonorRepository donorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            HospitalRepository hospitalRepository,
            DonorRepository donorRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.hospitalRepository = hospitalRepository;
        this.donorRepository = donorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String register(RegisterRequest request) {

        // Check whether the email is already registered.
        if (userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            return "Email already registered";
        }

        // Check whether the mobile number is already registered.
        if (userRepository
                .findByMobile(request.getMobile())
                .isPresent()) {

            return "Mobile number already registered";
        }

        LocalDateTime now = LocalDateTime.now();

        // Create the common user account.
        User user = new User();

        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(request.getRole());
        user.setIsVerified(false);
        user.setIsActive(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // Save the user first to obtain the generated user ID.
        User savedUser = userRepository.save(user);

        // Create the donor profile for DONOR registrations.
        if ("DONOR".equalsIgnoreCase(request.getRole())) {

            Donor donor = new Donor();

            donor.setUserId(savedUser.getUserId());
            donor.setFullName(request.getFullName());

            donor.setDateOfBirth(
                    LocalDate.parse(request.getDateOfBirth())
            );

            donor.setGender(request.getGender());
            donor.setBloodGroup(request.getBloodGroup());

            // New donors are unavailable until they explicitly
            // update their availability.
            donor.setIsAvailable(false);

            donor.setAddress(request.getAddress());
            donor.setCity(request.getCity());

            donor.setCreatedAt(now);
            donor.setUpdatedAt(now);

            donorRepository.save(donor);
        }

        // Preserve the existing hospital registration behaviour.
        if ("HOSPITAL".equalsIgnoreCase(request.getRole())) {

            Hospital hospital = new Hospital();

            hospital.setUserId(savedUser.getUserId());
            hospital.setHospitalName(
                    request.getOrganizationName()
            );
            hospital.setLicenseNumber(
                    request.getLicenseNumber()
            );
            hospital.setPhone(request.getMobile());
            hospital.setAddress(request.getAddress());
            hospital.setCity(request.getCity());
            hospital.setIsVerified(false);
            hospital.setCreatedAt(now);
            hospital.setUpdatedAt(now);

            hospitalRepository.save(hospital);
        }

        return "Registration successful";
    }
}
