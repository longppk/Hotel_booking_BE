package com.hotel.booking.services;

import com.hotel.booking.dtos.RoomCategoryDTO;
import com.hotel.booking.models.RoomCategory;
import com.hotel.booking.payload.PaginationResponse;
import com.hotel.booking.payload.request.DeleteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface RoomCategoryService {
    public List<RoomCategoryDTO> getNameCategory();
    public ResponseEntity<PaginationResponse> getRoomByCategory(Long categoryId, int size, int current);
    public RoomCategoryDTO getRoomCategoryDetail(Long categoryId);

    void deleteCategoryByIds(DeleteRequest deleteRequest);
    boolean addCategory(String name, String description, Long amenityId, MultipartFile image) throws IOException;
    boolean saveCategory(Long categoryId, String name, String description, Long amenityId, MultipartFile image) throws IOException;
    List<RoomCategory> search(String keyword);
}
