package com.example.uberauthservice.services;

import com.example.uberauthservice.dtos.DriverSignupRequestDTO;
import com.example.uberauthservice.dtos.DriverSignupResponseDTO;
import com.example.uberauthservice.repositories.DriverRepository;
import com.example.uberauthservice.repositories.RefreshTokenRepository;
import com.example.uberentityservice.enums.DriverApprovalStatus;
import com.example.uberentityservice.enums.UserType;
import com.example.uberentityservice.models.Driver;
import com.example.uberentityservice.models.RefreshToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class DriverAuthService {
    private final DriverRepository driverRepository;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public DriverAuthService(DriverRepository driverRepository, RefreshTokenRepository refreshTokenRepository, JWTService jwtService, RefreshTokenService refreshTokenService) {
        this.driverRepository = driverRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
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

    public DriverSignupResponseDTO login(String licenseNumber) {
        DriverSignupResponseDTO dto = new DriverSignupResponseDTO();
        Optional<Driver> driverOptional = driverRepository.findByLicenseNumber(licenseNumber);

        if (driverOptional.isEmpty()) {
            dto.setMessage("Driver Not Found");
            return dto;
        }

        Driver driver = driverOptional.get();
        dto.setDriver(driver);
        dto.setMessage("Driver Logged in successfully");

        String accessToken = jwtService.createToken(driver.getId(), driver.getLicenseNumber());
        dto.setAccessToken(accessToken);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .userId(driver.getId())
                .userType(UserType.DRIVER)
                .tokenHash(refreshTokenService.generateRefreshToken())
                .expiresAt(Instant.now().plusSeconds(refreshTokenService.getRefreshTokenExpiry()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        dto.setRefreshToken(refreshToken.getTokenHash());
        return dto;
    }
}
