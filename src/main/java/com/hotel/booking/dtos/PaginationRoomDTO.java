package com.hotel.booking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRoomDTO {
    private List<RoomDTO> rooms;
    private int pageCurrent;
    private int totalPage;
    private int totalResult;

}
