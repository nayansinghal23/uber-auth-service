package com.example.uberauthservice.services;

import com.example.uberauthservice.dtos.PassengerDTO;
import com.example.uberauthservice.dtos.PassengerSignupRequestDTO;
import com.example.uberauthservice.models.Passenger;
import com.example.uberauthservice.repositories.PassengerRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PassengerRepository passengerRepository;

    public AuthService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public PassengerDTO signup(PassengerSignupRequestDTO dto) {
        Passenger passenger = Passenger
                .builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword()) // TODO : Encrypt the password
                .phoneNumber(dto.getPhoneNumber())
                .build();
        Passenger newPassenger = passengerRepository.save(passenger);
        return PassengerDTO.from(newPassenger);
    }
}
