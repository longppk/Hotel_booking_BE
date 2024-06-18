package com.hotel.booking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String address;

    private String phone;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonIgnore
    private Review review;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Room> rooms = new ArrayList<>();
}
