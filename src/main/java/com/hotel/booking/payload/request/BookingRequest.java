package com.hotel.booking.payload.request;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    private Long roomId;
    private String status;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Double price;
    private String cardId;
}
