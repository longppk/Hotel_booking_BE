package com.hotel.booking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String description;

    private Double price;

    private boolean available;

    private Integer capacity;
    private String bed;
    private double length;
    private double width;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private RoomCategory category;

    @JsonIgnore
    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookingDetails = new ArrayList<>();

}
