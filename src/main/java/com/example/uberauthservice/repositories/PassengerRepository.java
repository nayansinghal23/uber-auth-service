package com.example.uberauthservice.repositories;

import com.example.uberentityservice.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);
    @Modifying
    @Query("UPDATE Passenger p SET p.passengerImage = :passengerImage WHERE p.id = :passengerId")
    int updatePassengerImage(
            @Param("passengerId") Long passengerId,
            @Param("passengerImage") String passengerImage
    );
}
