package com.example.uberauthservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerSignupRequestDTO {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
}
