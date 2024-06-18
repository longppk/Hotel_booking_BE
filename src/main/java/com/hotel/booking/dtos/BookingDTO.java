package com.hotel.booking.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;


@Data
public class BookingDTO {
    private Long id;

    private LocalDate checkIn;

    private LocalDate checkOut;
    private String status;
    private Double price;
    private String cardId;
    private ZonedDateTime bookingTime;

    private RoomDTO room;

    private UserDTO user;
}
