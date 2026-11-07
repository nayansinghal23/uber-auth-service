package com.example.uberauthservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileImageDTO {
    private String s3ObjectKey;
}
