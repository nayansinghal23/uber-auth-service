package com.example.uberauthservice.controllers;

import com.example.uberauthservice.dtos.PassengerDTO;
import com.example.uberauthservice.dtos.PassengerSigninRequestDTO;
import com.example.uberauthservice.services.AuthService;
import com.example.uberauthservice.services.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AuthControllerTests {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private JWTService jwtService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // scan the class, pick all the objects that have @Mock annotations and inject the values for them.
    }

    @Test
    public void testSigninSuccess() {
        Long userId = 3L;
        String email = "test1@gmail.com", password = "123456", token = "mock-jwt-token";

        PassengerSigninRequestDTO mockDTO = new PassengerSigninRequestDTO();
        mockDTO.setEmail(email);
        mockDTO.setPassword(password);

        PassengerDTO mockPassengerDTO = PassengerDTO
                .builder()
                .email(email)
                .userId(userId)
                .build();

        when(authService.signin(mockDTO)).thenReturn(mockPassengerDTO);
        when(jwtService.createToken(userId, email)).thenReturn(token);

        ResponseEntity<?> response = authController.signin(mockDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bearer " + token, response.getHeaders().getFirst("Authorization"));
    }
}