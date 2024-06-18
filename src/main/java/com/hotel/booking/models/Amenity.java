package com.hotel.booking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "amenities")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JsonIgnore
    @ManyToMany(mappedBy = "amenities")
    private List<RoomCategory> roomCategories = new ArrayList<>();
}
