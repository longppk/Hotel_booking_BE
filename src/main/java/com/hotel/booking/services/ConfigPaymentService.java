package com.hotel.booking.services;

import com.hotel.booking.dtos.VnPaymentDTO;
import com.hotel.booking.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;



public interface ConfigPaymentService {
    @Cacheable(value = "TxRef", key = "#email")
    ResponseEntity<MessageResponse> createUrlPayment(String email, Double totalPrice) throws UnsupportedEncodingException;

    String initTxRef();

    ResponseEntity<?> handlePaymentResult(@RequestBody VnPaymentDTO paymentRequest, String email);
}
