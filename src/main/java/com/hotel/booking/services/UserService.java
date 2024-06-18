package com.hotel.booking.services;

import com.hotel.booking.dtos.UserDTO;
import com.hotel.booking.models.Users;
import com.hotel.booking.payload.request.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    boolean addUser(SignUpRequest signUpRequest);

    ResponseEntity<String> forgotPass(String code, String mail);

    ResponseEntity<String> changePass(String mail, String newPass);

    String Authentication(String token);
    ResponseEntity<UserDTO> getProfile(String token);
    boolean saveProfile(String username, String fullname, String phone, String address, MultipartFile avatar, String token) throws IOException;
    void lockUserAccounts(List<Long> userIds);
    void unLockUserAccounts(List<Long> userIds);
}
