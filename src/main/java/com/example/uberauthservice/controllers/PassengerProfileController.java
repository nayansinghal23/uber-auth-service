package com.example.uberauthservice.controllers;

import com.example.uberauthservice.dtos.UpdateProfileImageDTO;
import com.example.uberauthservice.services.PassengerAuthService;
import com.example.uberentityservice.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/passenger")
public class PassengerProfileController {
    private final PassengerAuthService passengerAuthService;

    public PassengerProfileController(PassengerAuthService passengerAuthService) {
        this.passengerAuthService = passengerAuthService;
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

            int result = passengerAuthService.updateProfileImage(
                    userId,
                    dto.getS3ObjectKey()
            );

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{passengerId}")
    public ResponseEntity<?> getDriverDetails(@PathVariable Long passengerId) {
        Optional<Passenger> driver = passengerAuthService.getPassengerDetails(passengerId);
        if (driver.isPresent()) {
            return ResponseEntity.ok(driver.get());
        }
        return ResponseEntity.notFound().build();
    }
}
