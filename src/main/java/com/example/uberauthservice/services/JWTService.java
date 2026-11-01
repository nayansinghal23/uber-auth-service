package com.example.uberauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.accessTokenExpiry}")
    private long accessTokenExpiry;

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Long userId, String email) {

        long currentTime = System.currentTimeMillis();

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(new Date(currentTime))
                .expiration(
                        new Date(currentTime + accessTokenExpiry * 1000L)
                )
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token, Long userId) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject().equals(String.valueOf(userId));

        } catch (Exception e) {
            // Invalid signature, expired token, malformed token, etc.
            return false;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
}