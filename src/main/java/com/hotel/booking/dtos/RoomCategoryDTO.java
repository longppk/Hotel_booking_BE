package com.hotel.booking.dtos;

import com.hotel.booking.models.Amenity;
import com.hotel.booking.models.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private List<AmenityDTO> amenity;
}
