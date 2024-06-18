package com.hotel.booking.payload.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String role;
}
