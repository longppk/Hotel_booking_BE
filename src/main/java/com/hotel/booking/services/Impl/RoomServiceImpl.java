package com.hotel.booking.services.Impl;

import com.hotel.booking.dtos.*;
import com.hotel.booking.models.Image;
import com.hotel.booking.models.Room;
import com.hotel.booking.models.RoomCategory;
import com.hotel.booking.models.Users;
import com.hotel.booking.payload.request.AddRoomRequest;
import com.hotel.booking.payload.request.DeleteRequest;
import com.hotel.booking.repository.RoomCategoryRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import com.hotel.booking.services.RoomService;
import com.hotel.booking.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private RoomCategoryRepository roomCategoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    public PaginationRoomDTO findAvailableRooms(LocalDate checkIn, LocalDate checkOut, int requiredCapacity, List<Long> categoryIds, int pageCurrent, int pageSize, String sort) {
        List<Room> availableRooms;
        if (categoryIds == null || categoryIds.isEmpty()) {
            availableRooms = roomRepository.findAvailableRoomsByCheckInAndCheckOutAndCapacity(checkIn, checkOut, requiredCapacity);
        } else {
            availableRooms = roomRepository.findAvailableRoomsByCheckInAndCheckOutAndCapacityAndCategoryIds(checkIn, checkOut, requiredCapacity, categoryIds);
        }

        // Sort the list based on the 'sort' parameter
        if ("lowToHigh".equalsIgnoreCase(sort)) {
            availableRooms.sort(Comparator.comparing(Room::getPrice));
        } else if ("highToLow".equalsIgnoreCase(sort)) {
            availableRooms.sort(Comparator.comparing(Room::getPrice).reversed());
        }

        // Calculate pagination values
        int totalResult = availableRooms.size();
        int totalPage = (int) Math.ceil((double) totalResult / pageSize);

        // Ensure current page is within valid range
        if (pageCurrent < 1) {
            pageCurrent = 1;
        } else if (pageCurrent > totalPage) {
            pageCurrent = totalPage;
        }

        // Apply pagination
        int start = (pageCurrent - 1) * pageSize;
        int end = Math.min(start + pageSize, totalResult);

        // If start index is invalid, adjust it to 0
        if (start < 0) {
            start = 0;
        }

        List<RoomDTO> pageItems = availableRooms.subList(start, end).stream()
                .map(room -> {
                    RoomDTO roomDTO = new RoomDTO();
                    roomDTO.setId(room.getId());
                    roomDTO.setName(room.getName());
                    roomDTO.setDescription(room.getDescription());
                    roomDTO.setPrice(room.getPrice());
                    roomDTO.setCapacity(room.getCapacity());
                    roomDTO.setBed(room.getBed());
                    roomDTO.setLength(room.getLength());
                    roomDTO.setWidth(room.getWidth());
                    List<ImageDTO> imageDTOs = room.getImages().stream()
                            .map(image -> new ImageDTO(image.getId(), image.getUrlImage()))
                            .collect(Collectors.toList());
                    roomDTO.setImages(imageDTOs);
                    roomDTO.setRoomCategory(room.getCategory());
                    return roomDTO;
                }).collect(Collectors.toList());

        return new PaginationRoomDTO(pageItems, pageCurrent, totalPage, totalResult);
    }




    @Override
    public void deleteRoomByIds(DeleteRequest deleteRequest) {
        for (Long roomId : deleteRequest.getIds()) {
            roomRepository.deleteById(roomId);
        }
    }
    @Override
    public boolean addRoom(String name, String description, Double price, Integer capacity, String bed, Double length, Double width, Long categoryId, List<MultipartFile> files) {
        Room room = new Room();
        room.setName(name);
        room.setDescription(description);
        room.setPrice(price);
        room.setAvailable(true);
        room.setCapacity(capacity);
        room.setBed(bed);
        room.setLength(length);
        room.setWidth(width);
        RoomCategory roomCategory = roomCategoryRepository.findById(categoryId).orElse(null);
        room.setCategory(roomCategory);
        List<MultipartFile> images = files;
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile imageFile : images) {
                try {
                    String imageUrl = cloudinaryService.uploadImage(imageFile);
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                }
            }
            for (String imageUrl : imageUrls) {
                Image newImage = new Image();
                newImage.setUrlImage(imageUrl);
                newImage.setRoom(room);
                room.getImages().add(newImage);
            }
        }
        roomRepository.save(room);
        return true;
    }

    @Override
    public boolean saveRoom(Long id, String name, String description, Double price, Integer capacity, String bed, Double length, Double width, Long categoryId, List<MultipartFile> files) {
        Room room = roomRepository.findById(id).orElse(null);

        if (room != null) {
            room.setName(name);
            room.setDescription(description);
            room.setPrice(price);
            room.setAvailable(true);
            room.setCapacity(capacity);
            room.setBed(bed);
            room.setLength(length);
            room.setWidth(width);
            RoomCategory roomCategory = roomCategoryRepository.findById(categoryId).orElse(null);
            room.setCategory(roomCategory);
            if (files != null && !files.isEmpty()) {
                room.getImages().clear();
                for (MultipartFile imageFile : files) {
                    try {
                        String imageUrl = cloudinaryService.uploadImage(imageFile);
                        Image newImage = new Image();
                        newImage.setUrlImage(imageUrl);
                        newImage.setRoom(room);
                        room.getImages().add(newImage);
                    } catch (IOException e) {
                        System.out.println("Error" + e);
                    }
                }
            }
            roomRepository.save(room);
            return true;
        }
        return false;
    }

    @Override
    public RoomDTO getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setDescription(room.getDescription());
        roomDTO.setPrice(room.getPrice());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setBed(room.getBed());
        roomDTO.setLength(room.getLength());
        roomDTO.setWidth(room.getWidth());
        roomDTO.setRoomCategory(room.getCategory());
        List<ImageDTO> imageDTOs = room.getImages().stream()
                .map(image -> new ImageDTO(image.getId(), image.getUrlImage()))
                .collect(Collectors.toList());
        roomDTO.setImages(imageDTOs);
        return roomDTO;
    }

    @Transactional
    @Override
    public boolean checkRoomAvailable(List<Long> roomIds, LocalDate checkIn, LocalDate checkOut, String token) {
        String email = userService.Authentication(token);
        Optional<Users> users = userRepository.findByEmail(email);
        if (users.isEmpty()){
            return false;
        }
        for (Long roomId : roomIds) {
            Room room = roomRepository.findById(roomId).orElse(null);
            if (room == null || !room.isAvailable()) {
                return false; // Nếu phòng không tồn tại hoặc không khả dụng, trả về false ngay lập tức
            }
            if (!roomRepository.isRoomAvailable(checkIn, checkOut, roomId)) {
                return false; // Nếu phòng không khả dụng trong khoảng thời gian đã cho, trả về false ngay lập tức
            }
        }
        return true;
    }
}

