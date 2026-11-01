package com.example.uberauthservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRefreshDTO {
    private Long passengerId;
    private String refreshToken;
}
