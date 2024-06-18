package com.hotel.booking.services;

import com.hotel.booking.dtos.PaginationRoomDTO;
import com.hotel.booking.dtos.RoomDTO;
import com.hotel.booking.payload.request.DeleteRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface RoomService {
    public PaginationRoomDTO findAvailableRooms(LocalDate checkIn, LocalDate checkOut, int requiredCapacity, List<Long> categoryIds, int pageCurrent, int pageSize, String sort);
    public void deleteRoomByIds(DeleteRequest deleteRequest);

    public boolean addRoom(String name,
                           String description,
                           Double price,
                           Integer capacity,
                           String bed,
                           Double length,
                           Double width,
                           Long categoryId,
                           List<MultipartFile> files);
    public boolean saveRoom(
                            Long id,
                            String name,
                           String description,
                           Double price,
                           Integer capacity,
                           String bed,
                           Double length,
                           Double width,
                           Long categoryId,
                           List<MultipartFile> files);

    public RoomDTO getRoomById(Long roomId);

    public boolean checkRoomAvailable(List<Long> roomIds, LocalDate checkIn, LocalDate checkOut, String token);
}
