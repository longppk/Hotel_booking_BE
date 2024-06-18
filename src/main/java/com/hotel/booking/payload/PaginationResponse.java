package com.hotel.booking.payload;


import lombok.Data;

@Data
public class PaginationResponse {
    private Object data;
    private Pagination pagination;
}
