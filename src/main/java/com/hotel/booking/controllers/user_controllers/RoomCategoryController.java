package com.hotel.booking.controllers.user_controllers;


import com.hotel.booking.dtos.RoomCategoryDTO;
import com.hotel.booking.models.RoomCategory;
import com.hotel.booking.payload.request.DeleteRequest;
import com.hotel.booking.repository.RoomCategoryRepository;
import com.hotel.booking.services.RoomCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class RoomCategoryController {

    @Autowired
    RoomCategoryRepository roomCategoryRepository;

    @Autowired
    private RoomCategoryService roomCategoryService;

    @GetMapping("/category")
    public List<RoomCategory> getAllRoomCategory(){
        return roomCategoryRepository.findAll();
    }
    @GetMapping("/category/sort")
    public List<RoomCategory> getAllRoomSort(){
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return roomCategoryRepository.findAll(sort);
    }
    @PostMapping("/category/delete")
    public ResponseEntity<String> deleteRoomByIds(@RequestBody DeleteRequest deleteRequest){
        try {
            roomCategoryService.deleteCategoryByIds(deleteRequest);
            return new ResponseEntity<>("200", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("400", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/category/add")
    public boolean addRoom(@RequestParam (value = "name") String name,
                           @RequestParam (value ="description") String description,
                           @RequestParam (value = "amenityId") Long amenityId,
                           @RequestParam(value = "image") MultipartFile image) throws IOException {
       return roomCategoryService.addCategory(name, description, amenityId, image);
    }

    @PostMapping("/category/save")
    public boolean saveRoom(@RequestParam (value = "categoryId") Long categoryId,
                            @RequestParam (value = "name") String name,
                           @RequestParam (value ="description") String description,
                           @RequestParam (value = "amenityId") Long amenityId,
                           @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
       return roomCategoryService.saveCategory(categoryId,name, description, amenityId, image);
    }

    @GetMapping("/category/name")
    public List<RoomCategoryDTO> getCategoryName(){
        return roomCategoryService.getNameCategory();
    }

//    @GetMapping("/category/room/{roomCategoryId}")
//    public ResponseEntity<PaginationResponse> getAllRoomByCategoryId(@PathVariable Long categoryId, @RequestParam (value = "currentPage",defaultValue = "1", required = false) int currentPage, @RequestParam(value = "size", defaultValue = "3",required = false) int size){
//       return roomCategoryService.getRoomByCategory(categoryId, size, currentPage);
//    }

    @GetMapping("/category/{categoryId}")
    public RoomCategoryDTO getRoomCategoryById(@PathVariable Long categoryId){
        return roomCategoryService.getRoomCategoryDetail(categoryId);
    }

    @GetMapping("/category/search")
    public List<RoomCategory> getRoomBySearch(@RequestParam(value = "keyword") String keyword){
        keyword = keyword.trim();
        if (keyword.isEmpty()) {
            return roomCategoryRepository.findAll();
        }
        return roomCategoryRepository.findByNameContainingIgnoreCase( keyword);
    }
}
