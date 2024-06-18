package com.hotel.booking.dtos;


import com.hotel.booking.payload.request.BookingRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VnPaymentDTO {
    private String vnp_Amount;
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_PayDate;
    private String vnp_ResponseCode;
    private String vnp_SecureHash;
    private String vnp_TmnCode;
    private String vnp_TransactionNo;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private List<BookingRequest> bookingRequestList;
}
