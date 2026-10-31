package com.example.uberauthservice.controllers;

import com.example.uberauthservice.dtos.PassengerDTO;
import com.example.uberauthservice.dtos.PassengerSigninRequestDTO;
import com.example.uberauthservice.dtos.PassengerSignupRequestDTO;
import com.example.uberauthservice.services.JWTService;
import com.example.uberauthservice.services.PassengerAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/passenger")
public class PassengerAuthController {
    private final PassengerAuthService passengerAuthService;
    private final JWTService jwtService;

    public PassengerAuthController(PassengerAuthService passengerAuthService, JWTService jwtService) {
        this.passengerAuthService = passengerAuthService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody PassengerSignupRequestDTO dto) {
        try {
            PassengerDTO passengerDTO = passengerAuthService.signup(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(passengerDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody PassengerSigninRequestDTO dto) {
        try {
            PassengerDTO passengerDTO = passengerAuthService.login(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + passengerDTO.getAccessToken());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(passengerDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validate/{userId}")
    public ResponseEntity<Boolean> validatePassenger(@PathVariable Long userId, HttpServletRequest request) {
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
