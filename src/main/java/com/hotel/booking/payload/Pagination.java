package com.hotel.booking.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pagination {
    private int size;
    private int currentPage;
    private int totalPage;
    private int totalResult;
}
