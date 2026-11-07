package com.example.uberauthservice.repositories;

import com.example.uberentityservice.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    @Modifying
    @Query("UPDATE Driver d SET d.driverImage = :driverImage WHERE d.id = :driverId")
    int updateDriverImage(
            @Param("driverId") Long driverId,
            @Param("driverImage") String driverImage
    );
}
