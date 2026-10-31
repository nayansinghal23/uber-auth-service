package com.example.uberauthservice.dtos;


import com.example.uberentityservice.models.Passenger;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {
    private String name;
    private String email;
    private Long userId;
    private String phoneNumber;
    private Date createdAt;
    private String accessToken;
    private String refreshToken;

    public static PassengerDTO from(Passenger p, String accessToken, String refreshToken) {
        return PassengerDTO
                .builder()
                .userId(p.getId())
                .createdAt(p.getCreatedAt())
                .email(p.getEmail())
                .name(p.getName())
                .phoneNumber(p.getPhoneNumber())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
