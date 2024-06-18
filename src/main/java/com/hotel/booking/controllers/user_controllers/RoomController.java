package com.hotel.booking.controllers.user_controllers;


import com.hotel.booking.dtos.PaginationRoomDTO;
import com.hotel.booking.dtos.RoomDTO;
import com.hotel.booking.models.Room;
import com.hotel.booking.payload.request.DeleteRequest;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.services.BookingService;
import com.hotel.booking.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class RoomController {

    @Autowired
    private BookingService bookingDetailService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @GetMapping("/room")
    public List<Room> getAllRoom(){
        return roomRepository.findAll();
    }

    @GetMapping("/room/sort")
    public List<Room> getAllRoomSort(){
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return roomRepository.findAll(sort);
    }

    @PostMapping("/room/delete")
    public ResponseEntity<String> deleteRoomByIds(@RequestBody DeleteRequest deleteRequest){
        try {
            roomService.deleteRoomByIds(deleteRequest);
            return new ResponseEntity<>("200", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("400", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/room/add")
    public boolean addRoom(@RequestParam(value = "name") String name,
                           @RequestParam(value = "description") String description,
                           @RequestParam(value = "price") Double price,
                           @RequestParam(value = "capacity") Integer capacity,
                           @RequestParam(value = "bed") String bed,
                           @RequestParam(value = "length") Double length,
                           @RequestParam(value = "width") Double width,
                           @RequestParam(value = "categoryId") Long categoryId,
                           @RequestPart(value = "files")List<MultipartFile> files) throws IOException {
        return roomService.addRoom(name,description, price, capacity, bed,length,width, categoryId,files);
    }
    @GetMapping("/room/detail/{roomId}")
    public RoomDTO getRoomById(@PathVariable Long roomId){
        return roomService.getRoomById(roomId);
    }
    @PostMapping("/room/save")
    public boolean saveRoom(
                            @RequestParam(value = "roomId") Long roomId,
                            @RequestParam(value = "name") String name,
                           @RequestParam(value = "description") String description,
                           @RequestParam(value = "price") Double price,
                           @RequestParam(value = "capacity") Integer capacity,
                           @RequestParam(value = "bed") String bed,
                           @RequestParam(value = "length") Double length,
                           @RequestParam(value = "width") Double width,
                           @RequestParam(value = "categoryId") Long categoryId,
                           @RequestParam(value = "files", required = false)List<MultipartFile> files) throws IOException {
        return roomService.saveRoom(roomId,name,description, price, capacity, bed,length,width, categoryId,files);
    }


    @GetMapping("/available-rooms")
    public PaginationRoomDTO getAvailableRooms(
            @RequestParam("checkIn") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
            @RequestParam("checkOut") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
            @RequestParam("requiredCapacity") int requiredCapacity,
            @RequestParam(value = "categoryId", required = false) List<Long> categoryId,
            @RequestParam(value = "pageCurrent") int pageCurrent,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "sort") String sort) {

        return roomService.findAvailableRooms(checkIn, checkOut, requiredCapacity, categoryId, pageCurrent, pageSize, sort);
    }

    @GetMapping("/checkAvailable")
    public boolean checkAvailable(@RequestParam(value = "roomIds") List<Long> roomIds,
                                  @RequestParam(value = "checkIn") LocalDate checkIn,
                                  @RequestParam(value = "checkOut") LocalDate checkOut,
                                  @RequestHeader(value = "Authorization") String token){
        return roomService.checkRoomAvailable(roomIds, checkIn, checkOut, token);
    }

    @GetMapping("/room/total")
    public long totalRoom(){
        return roomRepository.count();
    }


}
