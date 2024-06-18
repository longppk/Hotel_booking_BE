package com.hotel.booking.repository;

import com.hotel.booking.models.Booking;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT bd FROM Booking bd WHERE bd.checkIn = :checkIn")
    List<Booking> findByCheckIn(LocalDate checkIn);
    @Query("SELECT bd FROM Booking bd WHERE bd.checkOut = :checkOut")
    List<Booking> findByCheckOut(LocalDate checkOut);

    @Query("SELECT bd FROM Booking bd WHERE bd.cardId LIKE %:cardId%")
    List<Booking> findByCardId(String cardId);

    @Query("SELECT bd FROM Booking bd WHERE bd.checkIn = :checkIn AND bd.cardId LIKE %:cardId%")
    List<Booking> findByCheckInAndCardId(@Param("checkIn") LocalDate checkIn, @Param("cardId") String cardId);

    @Query("SELECT bd FROM Booking bd WHERE bd.checkOut = :checkOut AND bd.cardId LIKE %:cardId%")
    List<Booking> findByCheckOutAndCardId(@Param("checkIn") LocalDate checkOut, @Param("cardId") String cardId);

    @Query("SELECT bd FROM Booking bd WHERE bd.checkIn <= :checkOut AND bd.checkOut >= :checkIn AND bd.cardId LIKE %:cardId%")
    List<Booking> findByCheckInAndCheckOutAndCardId(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut, @Param("cardId") String cardId);

    @Query("SELECT bd FROM Booking bd WHERE bd.checkIn <= :checkOut AND bd.checkOut >= :checkIn")
    List<Booking> findByCheckInAndCheckOut(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE (:newCheckIn <= b.checkOut) AND (:newCheckOut >= b.checkIn)")
    int SearchByCheckInAndCheckOut(@Param("newCheckIn") LocalDate newCheckIn,
                                 @Param("newCheckOut") LocalDate newCheckOut);


}
