package com.hotel.booking.repository;

import com.hotel.booking.dtos.RoomCategoryDTO;
import com.hotel.booking.models.Room;
import com.hotel.booking.models.RoomCategory;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Long> {
    Optional<RoomCategory> findById(Long category_id);
    boolean existsByName(String name);
    List<RoomCategory> findByNameContainingIgnoreCase(String name);
}
