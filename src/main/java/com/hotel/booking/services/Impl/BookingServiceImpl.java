package com.hotel.booking.services.Impl;

import com.hotel.booking.dtos.BookingDTO;
import com.hotel.booking.dtos.RoomDTO;
import com.hotel.booking.dtos.UserDTO;
import com.hotel.booking.models.Booking;
import com.hotel.booking.models.Room;
import com.hotel.booking.models.Users;
import com.hotel.booking.payload.request.BookingRequest;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import com.hotel.booking.services.BookingService;
import com.hotel.booking.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomRepository roomRepository;
    @Transactional
    @Override
    public ResponseEntity<String> bookingRoom(List<BookingRequest> bookingRequest, String token) {
        String email = userService.Authentication(token);
        Users users = userRepository.findByEmail(email).orElse(null);
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        for (BookingRequest request : bookingRequest) {
            Booking booking = new Booking();
            booking.setCheckIn(request.getCheckIn());
            booking.setCheckOut(request.getCheckOut());
            booking.setUser(users);
            booking.setStatus("SUCCESS");
            ZonedDateTime utcDateTime = ZonedDateTime.now(utcZone);
            ZonedDateTime vietnamDateTime = utcDateTime.withZoneSameInstant(vietnamZone).plusHours(7);
            booking.setBookingTime(vietnamDateTime);
            booking.setPrice(request.getPrice());
            booking.setCardId(request.getCardId());
            Room room = roomRepository.findById(request.getRoomId()).orElse(null);
            booking.setRoom(room);
            bookingRepository.save(booking);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Booking success");
    }

    @Override
    public List<BookingDTO> getAllBooking() {
        return bookingRepository.findAll().stream().
                map(booking -> {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(booking.getId());
                    bookingDTO.setCheckIn(booking.getCheckIn());
                    bookingDTO.setCheckOut(booking.getCheckOut());
                    bookingDTO.setStatus(booking.getStatus());
                    bookingDTO.setPrice(booking.getPrice());
                    bookingDTO.setCardId(booking.getCardId());
                    bookingDTO.setBookingTime(booking.getBookingTime());
                    RoomDTO roomDTO = new RoomDTO();
                    roomDTO.setId(booking.getRoom().getId());
                    roomDTO.setName(booking.getRoom().getName());
                    bookingDTO.setRoom(roomDTO);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(booking.getUser().getEmail());
                    bookingDTO.setUser(userDTO);
                    return bookingDTO;
                }).toList();
    }
    @Override
    public List<BookingDTO> getAllBookingByCheckIn(LocalDate checkIn) {
        return bookingRepository.findByCheckIn(checkIn).stream()
                .map(booking -> {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(booking.getId());
                    bookingDTO.setCheckIn(booking.getCheckIn());
                    bookingDTO.setCheckOut(booking.getCheckOut());
                    bookingDTO.setStatus(booking.getStatus());
                    bookingDTO.setPrice(booking.getPrice());
                    bookingDTO.setCardId(booking.getCardId());
                    bookingDTO.setBookingTime(booking.getBookingTime());
                    RoomDTO roomDTO = new RoomDTO();
                    roomDTO.setId(booking.getRoom().getId());
                    roomDTO.setName(booking.getRoom().getName());
                    bookingDTO.setRoom(roomDTO);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(booking.getUser().getEmail());
                    bookingDTO.setUser(userDTO);
                    return bookingDTO;
                }).toList();
    }

    @Override
    public List<BookingDTO> getAllBookingByCheckOut(LocalDate checkOut) {
        return bookingRepository.findByCheckOut(checkOut).stream()
                .map(booking -> {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(booking.getId());
                    bookingDTO.setCheckIn(booking.getCheckIn());
                    bookingDTO.setCheckOut(booking.getCheckOut());
                    bookingDTO.setStatus(booking.getStatus());
                    bookingDTO.setPrice(booking.getPrice());
                    bookingDTO.setCardId(booking.getCardId());
                    bookingDTO.setBookingTime(booking.getBookingTime());
                    RoomDTO roomDTO = new RoomDTO();
                    roomDTO.setId(booking.getRoom().getId());
                    roomDTO.setName(booking.getRoom().getName());
                    bookingDTO.setRoom(roomDTO);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(booking.getUser().getEmail());
                    bookingDTO.setUser(userDTO);
                    return bookingDTO;
                }).toList();
    }

    @Override
    public List<BookingDTO> getAllBookingByCardId(String cardId) {
        return bookingRepository.findByCardId(cardId).stream()
                .map(booking -> {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(booking.getId());
                    bookingDTO.setCheckIn(booking.getCheckIn());
                    bookingDTO.setCheckOut(booking.getCheckOut());
                    bookingDTO.setStatus(booking.getStatus());
                    bookingDTO.setPrice(booking.getPrice());
                    bookingDTO.setCardId(booking.getCardId());
                    bookingDTO.setBookingTime(booking.getBookingTime());
                    RoomDTO roomDTO = new RoomDTO();
                    roomDTO.setId(booking.getRoom().getId());
                    roomDTO.setName(booking.getRoom().getName());
                    bookingDTO.setRoom(roomDTO);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(booking.getUser().getEmail());
                    bookingDTO.setUser(userDTO);
                    return bookingDTO;
                }).toList();
    }

    @Override
    public List<BookingDTO> getAllBookingByCheckOutAndCard(LocalDate checkOut, String cardId) {
        return bookingRepository.findByCheckOutAndCardId(checkOut, cardId).stream()
                .map(booking -> {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(booking.getId());
                    bookingDTO.setCheckIn(booking.getCheckIn());
                    bookingDTO.setCheckOut(booking.getCheckOut());
                    bookingDTO.setStatus(booking.getStatus());
                    bookingDTO.setPrice(booking.getPrice());
                    bookingDTO.setCardId(booking.getCardId());
                    bookingDTO.setBookingTime(booking.getBookingTime());
                    RoomDTO roomDTO = new RoomDTO();
                    roomDTO.setId(booking.getRoom().getId());
                    roomDTO.setName(booking.getRoom().getName());
                    bookingDTO.setRoom(roomDTO);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(booking.getUser().getEmail());
                    bookingDTO.setUser(userDTO);
                    return bookingDTO;
                }).toList();
    }

    @Override
    public List<BookingDTO> getAllBookingByCheckInAndCard(LocalDate checkIn, String cardId) {
        return bookingRepository.findByCheckInAndCardId(checkIn, cardId).stream()
                .map(booking -> {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(booking.getId());
                    bookingDTO.setCheckIn(booking.getCheckIn());
                    bookingDTO.setCheckOut(booking.getCheckOut());
                    bookingDTO.setStatus(booking.getStatus());
                    bookingDTO.setPrice(booking.getPrice());
                    bookingDTO.setCardId(booking.getCardId());
                    bookingDTO.setBookingTime(booking.getBookingTime());
                    RoomDTO roomDTO = new RoomDTO();
                    roomDTO.setId(booking.getRoom().getId());
                    roomDTO.setName(booking.getRoom().getName());
                    bookingDTO.setRoom(roomDTO);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(booking.getUser().getEmail());
                    bookingDTO.setUser(userDTO);
                    return bookingDTO;
                }).toList();
    }


}
