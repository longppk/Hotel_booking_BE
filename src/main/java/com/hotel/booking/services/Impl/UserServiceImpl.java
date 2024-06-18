package com.hotel.booking.services.Impl;

import com.hotel.booking.dtos.UserDTO;
import com.hotel.booking.enums.Role;
import com.hotel.booking.models.Users;
import com.hotel.booking.payload.request.SignUpRequest;
import com.hotel.booking.repository.UserRepository;
import com.hotel.booking.services.JwtService;
import com.hotel.booking.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CodeTmpService codeTmpService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public boolean addUser(SignUpRequest signUpRequest) {
        Optional<Users> users = userRepository.findByEmail(signUpRequest.getUsername());
        if (users.isEmpty()) {
            Users user = new Users();
            user.setEmail(signUpRequest.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
            user.setRole((Role.USER));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public ResponseEntity<String> forgotPass(String code, String mail){
        if (codeTmpService.validateCode(mail, code)){
            return ResponseEntity.status(HttpStatus.OK).body("200");
        }
        else{
//			codeTmpService.deleteCode(mail);
            return ResponseEntity.status(HttpStatus.OK).body("400");
        }
    }

    @Override
    public ResponseEntity<String> changePass(String mail, String newPass){
        Users user = userRepository.findByEmail(mail).orElse(null);
        if(user != null){
            user.setPassword(bCryptPasswordEncoder.encode(newPass));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("200");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("400");
        }

    }

    @Override
    public String Authentication(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtService.extractUsername(token);
        Optional<Users> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            return username;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public ResponseEntity<UserDTO> getProfile(String token) {
        String email = Authentication(token);
        Users user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            UserDTO userDto = new UserDTO();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setPhone(user.getPhone());
            userDto.setAvatar(user.getAvatar());
            userDto.setAddress(user.getAddress());
            return ResponseEntity.ok(userDto);
        } else {
            return null;
        }
    }

    @Override
    public boolean saveProfile(String username, String fullname, String phone, String address, MultipartFile avatar, String token) throws IOException, IOException {
        String email = Authentication(token);
        Users user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }
        user.setUsername(username);
        user.setPhone(phone);
        user.setAddress(address);
        if (avatar != null){
            user.setAvatar(cloudinaryService.uploadImage(avatar));
        }
        userRepository.save(user);
        return true;

    }
    @Override
    @Transactional
    public void lockUserAccounts(List<Long> userIds) {
        List<Users> usersToLock = userRepository.findAllById(userIds);

        if (usersToLock.isEmpty()) {
            throw new RuntimeException("No users found with the provided IDs");
        }

        for (Users user : usersToLock) {
            user.setActive(false);
        }
        userRepository.saveAll(usersToLock);
    }

    @Override
    public void unLockUserAccounts(List<Long> userIds) {
        List<Users> usersToUnLock = userRepository.findAllById(userIds);

        if (usersToUnLock.isEmpty()) {
            throw new RuntimeException("No users found with the provided IDs");
        }

        for (Users user : usersToUnLock) {
            user.setActive(true);
        }
        userRepository.saveAll(usersToUnLock);
    }

}
