package com.example.uberauthservice.configurations;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {

    @Bean
    public S3Presigner s3Presigner(S3Properties properties) {
        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(
                        properties.getAccessKey(),
                        properties.getSecretKey()
                );

        return S3Presigner.builder()
                .region(Region.of(properties.getS3().getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials)
                )
                .build();
    }
}