package com.example.uberauthservice.controllers;

import com.example.uberauthservice.dtos.DriverSignupRequestDTO;
import com.example.uberauthservice.dtos.DriverSignupResponseDTO;
import com.example.uberauthservice.services.DriverAuthService;
import com.example.uberauthservice.services.JWTService;
import com.example.uberentityservice.models.Driver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth/driver")
public class DriverAuthController {
    private final DriverAuthService driverAuthService;
    private final JWTService jwtService;

    public DriverAuthController(DriverAuthService driverAuthService, JWTService jwtService) {
        this.driverAuthService = driverAuthService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<DriverSignupResponseDTO> driverSignup(@RequestBody DriverSignupRequestDTO dto) {
        try {
            Driver driver = driverAuthService.signup(dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            DriverSignupResponseDTO
                                    .builder()
                                    .driver(driver)
                                    .message("Signed up successfully")
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            DriverSignupResponseDTO
                                    .builder()
                                    .driver(null)
                                    .message(e.getMessage())
                                    .build()
                    );
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<DriverSignupResponseDTO> login(@RequestBody String licenseNumber) {
        DriverSignupResponseDTO responseDTO = new DriverSignupResponseDTO();
        try {
            Optional<Driver> driver = driverAuthService.login(licenseNumber);
            if (driver.isEmpty()) {
                responseDTO.setMessage("Unable to find driver with entered license number");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            responseDTO.setDriver(driver.get());
            responseDTO.setMessage("Driver logged in successfully");

            String token = jwtService.createToken(driver.get().getId(), driver.get().getLicenseNumber());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            return ResponseEntity.ok().headers(headers).body(responseDTO);
        } catch (Exception e) {
            responseDTO.setDriver(null);
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validate/{userId}")
    public ResponseEntity<Boolean> validateDriver(@PathVariable Long userId, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
            }

            boolean isTokenValid = jwtService.validateToken(authHeader.substring(7), userId);
            return new ResponseEntity<>(isTokenValid, isTokenValid ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
