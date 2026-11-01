package com.example.uberauthservice.dtos;

import com.example.uberentityservice.models.Driver;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverSignupResponseDTO {
    private Driver driver;
    private String message;
    private String accessToken;
    private String refreshToken;
}
