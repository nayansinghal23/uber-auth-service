package com.example.uberauthservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverSigninRequestDTO {
    public String licenseNumber;
}
