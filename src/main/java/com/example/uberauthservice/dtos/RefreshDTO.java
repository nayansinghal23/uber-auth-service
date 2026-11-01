package com.example.uberauthservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDTO {
    private Long userId;
    private String refreshToken;
}
