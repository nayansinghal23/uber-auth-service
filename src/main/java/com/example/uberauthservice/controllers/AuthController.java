package com.example.uberauthservice.controllers;

import com.example.uberauthservice.dtos.PassengerDTO;
import com.example.uberauthservice.dtos.PassengerSigninRequestDTO;
import com.example.uberauthservice.dtos.PassengerSignupRequestDTO;
import com.example.uberauthservice.services.AuthService;
import com.example.uberauthservice.services.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JWTService jwtService;

    public AuthController(AuthService authService, JWTService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDTO> signup(@RequestBody PassengerSignupRequestDTO dto) {
        PassengerDTO passengerDTO = authService.signup(dto);
        String token = jwtService.createToken(passengerDTO.getUserId(), passengerDTO.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(passengerDTO);
    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signin(@RequestBody PassengerSigninRequestDTO dto) {
        try {
            PassengerDTO passengerDTO = authService.signin(dto);
            String token = jwtService.createToken(passengerDTO.getUserId(), passengerDTO.getEmail());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(passengerDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validate/passenger/{userId}")
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
