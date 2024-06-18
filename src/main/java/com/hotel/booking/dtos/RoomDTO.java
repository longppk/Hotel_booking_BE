package com.hotel.booking.dtos;

import com.hotel.booking.models.RoomCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    private String name;

    private String description;

    private Double price;

    private Integer capacity;
    private String bed;
    private double length;
    private double width;
    private boolean available;

    List<ImageDTO> images;
    private RoomCategory roomCategory;
}
