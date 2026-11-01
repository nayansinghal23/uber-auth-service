package com.example.uberauthservice.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class RefreshTokenService {
    @Getter
    @Value("${jwt.refreshTokenExpiry}")
    private long refreshTokenExpiry;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateRefreshToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

}