package com.example.uberauthservice.dtos;

import jakarta.validation.constraints.NotBlank;

public record PresignedUrlRequest(

        @NotBlank
        String imageType,

        @NotBlank
        String contentType

) {}