package com.hotel.booking.controllers.user_controllers;


import com.hotel.booking.models.Amenity;
import com.hotel.booking.repository.AmenitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class AmenityController {

    @Autowired
    private AmenitiesRepository amenitiesRepository;

    @GetMapping("/amenities")
    public List<Amenity> getAllAmenity(){
        return amenitiesRepository.findAll();
    }
}
