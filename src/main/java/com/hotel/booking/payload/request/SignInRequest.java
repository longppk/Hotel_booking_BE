package com.hotel.booking.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String username ;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 160, message = "Mật khẩu phải từ 6 đến 160 ký tự")
    private String password;
}
