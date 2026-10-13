package com.example.uberauthservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerSigninRequestDTO {
    private String email;
    private String password;
}
