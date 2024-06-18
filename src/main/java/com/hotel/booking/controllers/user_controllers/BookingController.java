package com.hotel.booking.controllers.user_controllers;


import com.hotel.booking.dtos.BookingDTO;
import com.hotel.booking.dtos.VnPaymentDTO;
import com.hotel.booking.models.Users;
import com.hotel.booking.payload.request.BookingRequest;
import com.hotel.booking.payload.response.MessageResponse;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.UserRepository;
import com.hotel.booking.services.BookingService;
import com.hotel.booking.services.ConfigPaymentService;
import com.hotel.booking.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfigPaymentService configPaymentService;

    @Autowired
    private BookingService bookingDetailService;

    @Autowired
    private BookingRepository bookingDetailRepository;

    @PostMapping("/checkout/createUrl")
    public ResponseEntity<MessageResponse> createUrlPayment(@RequestParam(value = "totalPrice") Double totalPrice, @RequestHeader(name = "Authorization") String token) throws UnsupportedEncodingException {
        return configPaymentService.createUrlPayment(token,totalPrice);
    }

    @PostMapping("/checkout/checkResponse")
    public ResponseEntity<?> createUrlPayment(@RequestBody VnPaymentDTO vnPaymentDTO, @RequestHeader(name = "Authorization") String token ) {
        System.out.println(vnPaymentDTO);
        return configPaymentService.handlePaymentResult(vnPaymentDTO,token);
    }


    @PostMapping("/room")
    public ResponseEntity<String> bookingRoom(@RequestBody List<BookingRequest> bookingRequests, @RequestHeader(name = "Authorization") String token){
        return bookingDetailService.bookingRoom(bookingRequests, token);
    }

    @GetMapping("/all")
    public List<BookingDTO> getAllBooking(){
        return bookingDetailService.getAllBooking();
    }

    @GetMapping("/all/checkIn")
    public List<BookingDTO> getAllByCheckIn(@RequestParam(value = "checkIn") LocalDate checkIn){
            return bookingDetailService.getAllBookingByCheckIn(checkIn);
    }

    @GetMapping("/all/checkOut")
    public List<BookingDTO> getAllByCheckOut(@RequestParam(value = "checkOut") LocalDate checkOut){
        return bookingDetailService.getAllBookingByCheckOut(checkOut);
    }
    @GetMapping("/all/cardId")
    public List<BookingDTO> getAllByCardId(@RequestParam(value = "cardId") String cardId){
        return bookingDetailService.getAllBookingByCardId(cardId);
    }

    @GetMapping("/all/checkOutAndCardId")
    public List<BookingDTO> getAllByCheckOutAndCardId(@RequestParam(value = "checkOut") LocalDate checkOut, @RequestParam(value = "cardId") String cardId){
        return bookingDetailService.getAllBookingByCheckOutAndCard(checkOut, cardId);
    }
    @GetMapping("/all/checkInAndCardId")
    public List<BookingDTO> getAllByCheckInAndCardId(@RequestParam(value = "checkIn") LocalDate checkIn, @RequestParam(value = "cardId") String cardId){
        return bookingDetailService.getAllBookingByCheckInAndCard(checkIn, cardId);
    }
}
