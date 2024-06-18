package com.hotel.booking.services.Impl;

import com.hotel.booking.dtos.AmenityDTO;
import com.hotel.booking.dtos.ImageDTO;
import com.hotel.booking.dtos.RoomCategoryDTO;
import com.hotel.booking.dtos.RoomDTO;
import com.hotel.booking.models.Amenity;
import com.hotel.booking.models.Room;
import com.hotel.booking.models.RoomCategory;
import com.hotel.booking.payload.Pagination;
import com.hotel.booking.payload.PaginationResponse;
import com.hotel.booking.payload.request.DeleteRequest;
import com.hotel.booking.repository.AmenitiesRepository;
import com.hotel.booking.repository.RoomCategoryRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.services.RoomCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomCategoryServiceImpl implements RoomCategoryService {

    @Autowired
    private RoomCategoryRepository roomCategoryRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private AmenitiesRepository amenitiesRepository;
    @Override
    public List<RoomCategoryDTO> getNameCategory() {
        List<RoomCategory> roomCategories = roomCategoryRepository.findAll();
        List<RoomCategoryDTO> roomCategoryDTOS = new ArrayList<>();
        for(RoomCategory roomCategory : roomCategories){
            RoomCategoryDTO roomCategoryDTO = new RoomCategoryDTO();
            roomCategoryDTO.setId(roomCategory.getId());
            roomCategoryDTO.setName(roomCategory.getName());
            roomCategoryDTOS.add(roomCategoryDTO);
        }
        return roomCategoryDTOS;
    }

    @Override
    public ResponseEntity<PaginationResponse> getRoomByCategory(Long categoryId, int size, int current) {
        Pageable pageable = PageRequest.of(current-1, size);

        RoomCategory roomCategory = roomCategoryRepository.findById(categoryId).orElseThrow(null);
        Page<Room> roomList = roomRepository.findByCategory(roomCategory,pageable);
        List<RoomDTO> roomDTOS = roomList.getContent().stream().map(item -> {
            RoomDTO roomDTO = new RoomDTO();
//            if(!item.getAvailable()){
//                return null;
//            }
            roomDTO.setId(item.getId());
            roomDTO.setName(item.getName());
            roomDTO.setDescription(item.getDescription());
            roomDTO.setPrice(item.getPrice());
            roomDTO.setCapacity(item.getCapacity());
            List<ImageDTO> imageDTOS = item.getImages().stream().map(image -> {
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setId(image.getId());
                imageDTO.setImageUrl(image.getUrlImage());
                return imageDTO;
            }).collect(Collectors.toList());
            roomDTO.setImages(imageDTOS);
            return roomDTO;
        }).toList();
        Pagination pagination = Pagination.builder()
                .currentPage(current)
                .size(size)
                .totalPage(roomList.getTotalPages())
                .totalResult((int) roomList.getTotalElements())
                .build();
        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setData(roomDTOS);
        paginationResponse.setPagination(pagination);
        return ResponseEntity.ok(paginationResponse);
    }

    @Override
    public RoomCategoryDTO getRoomCategoryDetail(Long categoryId) {
        Optional<RoomCategory> roomCategoryOptional = roomCategoryRepository.findById(categoryId);
        RoomCategory roomCategory = roomCategoryOptional.get();
        RoomCategoryDTO roomCategoryDTO = new RoomCategoryDTO();
        roomCategoryDTO.setId(roomCategory.getId());
        roomCategoryDTO.setName(roomCategory.getName());
        roomCategoryDTO.setDescription(roomCategory.getDescription());
        roomCategoryDTO.setImage(roomCategory.getImage());
        List<AmenityDTO> amenityDTOS = roomCategory.getAmenities().stream()
                .map(amenity -> {
                    AmenityDTO amenityDTO = new AmenityDTO();
                    amenityDTO.setId(amenity.getId());
                    amenityDTO.setName(amenity.getName());
                    amenityDTO.setLength(amenity.getLength());
                    amenityDTO.setWidth(amenity.getWidth());
                    amenityDTO.setBed(amenity.getBed());
                    amenityDTO.setCapacity(amenity.getCapacity());
                    amenityDTO.setWifi(amenity.getWifi());
                    amenityDTO.setSafety(amenity.getSafety());
                    amenityDTO.setView(amenity.getView());
                    amenityDTO.setFood(amenity.getFood());
                    amenityDTO.setTivi(amenity.getTivi());
                    amenityDTO.setBathRoom(amenity.getBathRoom());
                    amenityDTO.setHousekeeping(amenity.getHousekeeping());
                    return amenityDTO;
                }).toList();
        roomCategoryDTO.setAmenity(amenityDTOS);
        return roomCategoryDTO;
    }

    @Override
    public void deleteCategoryByIds(DeleteRequest deleteRequest) {
        for (Long categoryId : deleteRequest.getIds()) {
            roomCategoryRepository.deleteById(categoryId);
        }
    }

    @Override
    public boolean addCategory(String name, String description, Long id, MultipartFile image) throws IOException {
        if(roomCategoryRepository.existsByName(name)){
            return false;
        }
        RoomCategory roomCategory = new RoomCategory();
        roomCategory.setName(name);
        roomCategory.setDescription(description);
        Amenity amenity = amenitiesRepository.findById(id).orElse(null);
        roomCategory.getAmenities().add(amenity);
        roomCategory.setImage(cloudinaryService.uploadImage(image));
        roomCategoryRepository.save(roomCategory);
        return true;
    }

    @Override
    public boolean saveCategory(Long categoryId, String name, String description, Long amenityId, MultipartFile image) throws IOException {
        RoomCategory roomCategory = roomCategoryRepository.findById(categoryId).orElse(null);
        if (roomCategory == null){
            return false;
        }
            roomCategory.setName(name);
            roomCategory.setDescription(description);
            Amenity amenity = amenitiesRepository.findById(amenityId).orElse(null);
            List<Amenity> amenities = new ArrayList<>();
            amenities.add(amenity);
            roomCategory.setAmenities(amenities);
            if(image != null){
                roomCategory.setImage(cloudinaryService.uploadImage(image));
            }
            roomCategoryRepository.save(roomCategory);
            return true;
    }

    @Override
    public List<RoomCategory> search(String keyword) {
        return roomCategoryRepository.findByNameContainingIgnoreCase( keyword);
    }
}
