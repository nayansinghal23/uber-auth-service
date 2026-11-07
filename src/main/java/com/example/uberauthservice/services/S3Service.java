package com.example.uberauthservice.services;

import com.example.uberauthservice.configurations.S3Properties;
import com.example.uberauthservice.dtos.PresignedUrlResponse;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Properties properties;

    private static final Duration URL_EXPIRATION =
            Duration.ofMinutes(5);

    private static final Map<String, String> ALLOWED_CONTENT_TYPES =
            Map.of(
                    "image/jpeg", "jpg",
                    "image/png", "png",
                    "image/webp", "webp"
            );

    public S3Service(
            S3Presigner s3Presigner,
            S3Properties properties
    ) {
        this.s3Presigner = s3Presigner;
        this.properties = properties;
    }

    public PresignedUrlResponse generatePresignedUrl(
            String imageType,
            String contentType
    ) {

        String normalizedType = imageType.toUpperCase();

        if (!normalizedType.equals("PASSENGER")
                && !normalizedType.equals("DRIVER")) {
            throw new IllegalArgumentException("Invalid image type");
        }

        String extension = ALLOWED_CONTENT_TYPES.get(contentType);

        if (extension == null) {
            throw new IllegalArgumentException(
                    "Unsupported image content type"
            );
        }

        String objectKey = String.format(
                "profile-images/%s/%s.%s",
                normalizedType.toLowerCase(),
                UUID.randomUUID(),
                extension
        );

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(properties.getS3().getBucket())
                        .key(objectKey)
                        .contentType(contentType)
                        .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(URL_EXPIRATION)
                        .putObjectRequest(putObjectRequest)
                        .build();

        String uploadUrl = s3Presigner
                .presignPutObject(presignRequest)
                .url()
                .toString();

        return new PresignedUrlResponse(
                uploadUrl,
                objectKey,
                URL_EXPIRATION.toSeconds()
        );
    }
}