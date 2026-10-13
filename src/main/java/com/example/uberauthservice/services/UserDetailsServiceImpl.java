package com.example.uberauthservice.services;

import com.example.uberauthservice.helpers.AuthPassengerDetails;
import com.example.uberauthservice.models.Passenger;
import com.example.uberauthservice.repositories.PassengerRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PassengerRepository passengerRepository;

    public UserDetailsServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Optional<Passenger> passenger = passengerRepository.findByEmail(username);
        if (passenger.isEmpty()) throw new UsernameNotFoundException("User not found with email : " + username);
        return new AuthPassengerDetails(passenger.get());
    }
}
