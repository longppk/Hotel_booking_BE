package com.hotel.booking.payload.request;


import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String password;
}
