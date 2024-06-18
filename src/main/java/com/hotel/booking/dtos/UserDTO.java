package com.hotel.booking.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;


@Data
public class UserDTO {
    private Long id;
    private String username;
    private String fullname;

    private String email;

    private String password;

    private String avatar;
    private String phone;
    private String address;
}
