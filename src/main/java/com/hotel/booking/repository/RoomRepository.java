package com.hotel.booking.repository;

import com.hotel.booking.models.Room;
import com.hotel.booking.models.RoomCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByCategory(RoomCategory roomCategory, Pageable pageable);

    @Query("SELECT r FROM Room r " +
            "LEFT JOIN FETCH r.images i " +
            "LEFT JOIN FETCH r.category rc " +
            "WHERE (:categoryIds IS NULL OR rc.id IN :categoryIds) " +
            "AND r.capacity >= :requiredCapacity " +
            "AND r.available = true " +
            "AND NOT EXISTS (" +
            "    SELECT 1 FROM Booking bd " +
            "    WHERE bd.room.id = r.id " +
            "    AND ((:checkIn BETWEEN bd.checkIn AND bd.checkOut) " +
            "       OR (:checkOut BETWEEN bd.checkIn AND bd.checkOut) " +
            "       OR (bd.checkIn BETWEEN :checkIn AND :checkOut) " +
            "       OR (bd.checkOut BETWEEN :checkIn AND :checkOut)))")
    List<Room> findAvailableRoomsByCheckInAndCheckOutAndCapacityAndCategoryIds(@Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut,
                                  @Param("requiredCapacity") int requiredCapacity,
                                  @Param("categoryIds") List<Long> categoryIds);

    @Query("SELECT r FROM Room r " +
            "LEFT JOIN FETCH r.images i " +
            "LEFT JOIN FETCH r.category rc " +
            "WHERE r.capacity >= :requiredCapacity " +
            "AND r.available = true " +
            "AND NOT EXISTS (" +
            "    SELECT 1 FROM Booking bd " +
            "    WHERE bd.room.id = r.id " +
            "    AND ((:checkIn BETWEEN bd.checkIn AND bd.checkOut) " +
            "       OR (:checkOut BETWEEN bd.checkIn AND bd.checkOut) " +
            "       OR (bd.checkIn BETWEEN :checkIn AND :checkOut) " +
            "       OR (bd.checkOut BETWEEN :checkIn AND :checkOut)))")
    List<Room> findAvailableRoomsByCheckInAndCheckOutAndCapacity(@Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut,
                                  @Param("requiredCapacity") int requiredCapacity);


    @Query("SELECT CASE WHEN COUNT(bd) = 0 THEN true ELSE false END " +
            "FROM Booking bd " +
            "WHERE bd.room.id = :roomId " +
            "AND NOT ((:checkOut <= bd.checkIn) OR (:checkIn >= bd.checkOut))")
    boolean isRoomAvailable(@Param("checkIn") LocalDate checkIn,
                            @Param("checkOut") LocalDate checkOut,
                            @Param("roomId") Long roomId);

}
