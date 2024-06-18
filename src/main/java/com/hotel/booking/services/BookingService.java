package com.hotel.booking.services;

import com.hotel.booking.dtos.BookingDTO;
import com.hotel.booking.payload.request.BookingRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    public ResponseEntity<String> bookingRoom(List<BookingRequest> bookingRequest, String token);

    public List<BookingDTO> getAllBooking();
    public List<BookingDTO> getAllBookingByCheckIn(LocalDate checkIn);
    public List<BookingDTO> getAllBookingByCheckOut(LocalDate checkOut);
    public List<BookingDTO> getAllBookingByCardId(String cardId);
    public List<BookingDTO> getAllBookingByCheckOutAndCard(LocalDate checkOut, String cardId);
    public List<BookingDTO> getAllBookingByCheckInAndCard(LocalDate checkIn, String cardId);
//    public List<BookingDTO> getAAllBookingByCheckInAndCheckOut(LocalDate checkIn, LocalDate checkOut);
//    public List<BookingDTO> getAllBookingByCheckInAndCheckOutAndCardId(LocalDate checkInDate, LocalDate checkOut, String cardId);
}
