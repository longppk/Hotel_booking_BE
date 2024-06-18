package com.hotel.booking.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDTO {
    private Long id;
    private String imageUrl;
    public ImageDTO(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

}
