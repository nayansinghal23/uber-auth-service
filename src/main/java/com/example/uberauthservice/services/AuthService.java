package com.example.uberauthservice.services;

import com.example.uberauthservice.dtos.PassengerDTO;
import com.example.uberauthservice.dtos.PassengerSigninRequestDTO;
import com.example.uberauthservice.dtos.PassengerSignupRequestDTO;
import com.example.uberauthservice.repositories.PassengerRepository;
import com.example.uberentityservice.models.Passenger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final PassengerRepository passengerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(PassengerRepository passengerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.passengerRepository = passengerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public PassengerDTO signup(PassengerSignupRequestDTO dto) {
        Passenger passenger = Passenger
                .builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber())
                .build();
        Passenger newPassenger = passengerRepository.save(passenger);
        return PassengerDTO.from(newPassenger);
    }

    public PassengerDTO signin(PassengerSigninRequestDTO dto) {
        Optional<Passenger> passenger = passengerRepository.findByEmail(dto.getEmail());
        if (passenger.isEmpty()) throw new UsernameNotFoundException("Invalid email : " + dto.getEmail());

        if (!bCryptPasswordEncoder.matches(dto.getPassword(), passenger.get().getPassword())) {
            throw new UsernameNotFoundException("Password mismatched : " + dto.getPassword());
        }

        return PassengerDTO.from(passenger.get());
    }
}
