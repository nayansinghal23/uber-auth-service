package com.example.uberauthservice.dtos;

public record PresignedUrlResponse(
        String uploadUrl,
        String objectKey,
        long expiresInSeconds
) {}