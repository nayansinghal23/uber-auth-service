package com.example.uberauthservice.services;

import com.example.uberauthservice.dtos.DriverSignupRequestDTO;
import com.example.uberauthservice.repositories.DriverRepository;
import com.example.uberentityservice.enums.DriverApprovalStatus;
import com.example.uberentityservice.models.Driver;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverAuthService {
    private final DriverRepository driverRepository;

    public DriverAuthService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver signup(DriverSignupRequestDTO dto) {
        return driverRepository.save(
            Driver
                .builder()
                .name(dto.getName())
                .licenseNumber(dto.getLicenseNumber())
                    .rating(5.0)
                .driverApprovalStatus(DriverApprovalStatus.APPROVED) // TODO : PENDING approval status and a CRON job to convert it to APPROVED.
                .build()
        );
    }

    public Optional<Driver> login(String licenseNumber) {
        return driverRepository.findByLicenseNumber(licenseNumber);
    }
}
