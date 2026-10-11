package com.example.uberauthservice.dtos;

import com.example.uberauthservice.models.Passenger;
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
    private String password;
    private Long userId;
    private String phoneNumber;
    private Date createdAt;

    public static PassengerDTO from(Passenger p) {
        return PassengerDTO
                .builder()
                .userId(p.getId())
                .createdAt(p.getCreatedAt())
                .email(p.getEmail())
                .name(p.getName())
                .password(p.getPassword())
                .phoneNumber(p.getPhoneNumber())
                .build();
    }
}
