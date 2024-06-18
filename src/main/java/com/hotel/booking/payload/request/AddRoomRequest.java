package com.hotel.booking.payload.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.booking.models.Image;
import com.hotel.booking.models.RoomCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoomRequest {
    private String name;

    private String description;

    private Double price;

    private Boolean available;

    private Integer capacity;
    private String bed;
    private double length;
    private double width;

    private Long categoryId;

    private List<MultipartFile> files;
}
