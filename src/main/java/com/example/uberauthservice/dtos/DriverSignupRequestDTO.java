package com.example.uberauthservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverSignupRequestDTO {
    private String name;
    private String licenseNumber;
}
