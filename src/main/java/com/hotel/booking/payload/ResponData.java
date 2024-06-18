package com.hotel.booking.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponData {
    private int status = 200;
    private boolean isSuccess = true;
    private String description;
    private Object data;
}
