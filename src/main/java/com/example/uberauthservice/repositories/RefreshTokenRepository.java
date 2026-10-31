package com.example.uberauthservice.repositories;

import com.example.uberentityservice.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
