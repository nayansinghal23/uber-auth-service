package com.example.uberauthservice.controllers;

import com.example.uberauthservice.dtos.PresignedUrlRequest;
import com.example.uberauthservice.dtos.PresignedUrlResponse;
import com.example.uberauthservice.services.S3Service;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(
            @Valid @RequestBody PresignedUrlRequest request
    ) {
        PresignedUrlResponse response =
                s3Service.generatePresignedUrl(
                        request.imageType(),
                        request.contentType()
                );

        return ResponseEntity.ok(response);
    }
}
