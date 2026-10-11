package com.example.uberauthservice.dtos;

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
}
