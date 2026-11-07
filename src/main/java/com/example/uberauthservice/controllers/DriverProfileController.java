package com.example.uberauthservice.controllers;

import com.example.uberauthservice.dtos.UpdateProfileImageDTO;
import com.example.uberauthservice.services.DriverAuthService;
import com.example.uberentityservice.models.Driver;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverProfileController {
    private final DriverAuthService driverAuthService;

    public DriverProfileController(DriverAuthService driverAuthService) {
        this.driverAuthService = driverAuthService;
    }

    @PutMapping("/profile-image")
    public ResponseEntity<Integer> updateProfileImage(
            @RequestBody UpdateProfileImageDTO dto
    ) {
        try {
            Long userId = (Long) Objects.requireNonNull(SecurityContextHolder
                            .getContext()
                            .getAuthentication())
                    .getPrincipal();
            if (userId == null) {
                throw new Exception("User is not logged in");
            }

            int result = driverAuthService.updateProfileImage(
                    userId,
                    dto.getS3ObjectKey()
            );

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<?> getDriverDetails(@PathVariable Long driverId) {
        Optional<Driver> driver = driverAuthService.getDriverDetails(driverId);
        if (driver.isPresent()) {
            return ResponseEntity.ok(driver.get());
        }
        return ResponseEntity.notFound().build();
    }
}
