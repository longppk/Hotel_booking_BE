package com.hotel.booking.repository;

import com.hotel.booking.models.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenity, Long> {
    Optional<Amenity> findById(Long id);
}
