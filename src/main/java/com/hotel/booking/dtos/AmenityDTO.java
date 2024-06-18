package com.hotel.booking.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityDTO {
    private Long id;
    private String name;
    private double length;
    private double width;
    private String bed;
    private int capacity;
    private String wifi;
    private String safety;
    private String view;
    private String food;
    private String tivi;
    private String bathRoom;
    private String housekeeping;
    private String laundry;
}
