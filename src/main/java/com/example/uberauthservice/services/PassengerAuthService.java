package com.example.uberauthservice.services;

import com.example.uberauthservice.dtos.PassengerDTO;
import com.example.uberauthservice.dtos.PassengerSigninRequestDTO;
import com.example.uberauthservice.dtos.PassengerSignupRequestDTO;
import com.example.uberauthservice.repositories.PassengerRepository;
import com.example.uberauthservice.repositories.RefreshTokenRepository;
import com.example.uberentityservice.enums.UserType;
import com.example.uberentityservice.models.Passenger;
import com.example.uberentityservice.models.RefreshToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class PassengerAuthService {
    private final PassengerRepository passengerRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;

    public PassengerAuthService(PassengerRepository passengerRepository, RefreshTokenRepository refreshTokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTService jwtService, RefreshTokenService refreshTokenService) {
        this.passengerRepository = passengerRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
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
        return PassengerDTO.from(newPassenger, null, null);
    }

    public PassengerDTO login(PassengerSigninRequestDTO dto) {
        Optional<Passenger> passenger = passengerRepository.findByEmail(dto.getEmail());
        if (passenger.isEmpty()) throw new UsernameNotFoundException("Invalid email : " + dto.getEmail());

        if (!bCryptPasswordEncoder.matches(dto.getPassword(), passenger.get().getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String accessToken = jwtService.createToken(passenger.get().getId(), passenger.get().getEmail());
        RefreshToken refreshToken = RefreshToken
                .builder()
                .userId(passenger.get().getId())
                .userType(UserType.RIDER)
                .tokenHash(refreshTokenService.generateRefreshToken())
                .expiresAt(Instant.now().plusSeconds(refreshTokenService.getRefreshTokenExpiry()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return PassengerDTO.from(passenger.get(), accessToken, refreshToken.getTokenHash());
    }

    public String refresh(Long passengerId, String refreshToken) {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if (passenger.isEmpty()) throw new UsernameNotFoundException("Invalid passenger id : " + passengerId);

        Optional<RefreshToken> storedToken = refreshTokenRepository.findByUserId(passengerId);
        if (storedToken.isEmpty()) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        RefreshToken token = storedToken.get();

        // Validate token ownership/type
        if (!token.getUserType().equals(UserType.RIDER)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        // Reject revoked tokens
        if (token.isRevoked()) {
            throw new BadCredentialsException("Refresh token has been revoked");
        }

        // Validate token value
        if (!token.getTokenHash().equals(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        // Validate token expiry
        if (!token.getExpiresAt().isAfter(Instant.now())) {
            throw new BadCredentialsException("Refresh token has expired");
        }

        // Generate a new access token using your existing JWT service
        return jwtService.createToken(passengerId, passenger.get().getEmail());
    }

    public void logout(Long passengerId) {
        refreshTokenRepository.deleteByUserId(passengerId);
    }
}
