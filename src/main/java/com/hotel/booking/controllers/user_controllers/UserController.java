package com.hotel.booking.controllers.user_controllers;


import com.hotel.booking.dtos.UserDTO;
import com.hotel.booking.enums.Role;
import com.hotel.booking.models.Users;
import com.hotel.booking.payload.ResponData;
import com.hotel.booking.payload.request.SignInRequest;
import com.hotel.booking.payload.request.SignUpRequest;
import com.hotel.booking.payload.response.AuthResponse;
import com.hotel.booking.repository.UserRepository;
import com.hotel.booking.services.JwtService;
import com.hotel.booking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody SignInRequest signInRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<Users> user = userRepository.findByEmail(userDetails.getUsername());

            if (user.isPresent() && user.get().isActive()) {
                String token = jwtService.generateToken(userDetails.getUsername(), user.get().getRole().name());
                AuthResponse authResponse = new AuthResponse();
                authResponse.setToken(token);
                authResponse.setRole(user.get().getRole().name());
                return ResponseEntity.status(HttpStatus.OK).body(authResponse);
            } else {
                AuthResponse authResponse = new AuthResponse();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không tồn tại hoặc đã bị khóa.");
            }
        } catch (AuthenticationException e) {
            AuthResponse authResponse = new AuthResponse();
            if (e instanceof LockedException) {
                return ResponseEntity.status(HttpStatus.LOCKED).body("Tài khoản bạn hiện đang bị khóa.");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản hoặc mật khẩu không chính xác.");
        }
    }


    @PostMapping("/signup")
    public boolean signup(@RequestBody SignUpRequest signUpRequest) {
        return userService.addUser(signUpRequest);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePass(@RequestBody Map<String, String> resData) {
        String password = resData.get("password");
        String mail = resData.get("username");
        return userService.changePass(mail, password);
    }

    @GetMapping("/getUsername")
    public ResponseEntity<String> getUsername(@RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.Authentication(token));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(@RequestHeader(name = "Authorization") String token) {
        return userService.getProfile(token);
    }

    @PostMapping("/profile/save")
    public boolean saveProfile(
            @RequestParam("username") String userName,
            @RequestParam("fullname") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            @RequestHeader(name = "Authorization") String token) throws IOException {
        return userService.saveProfile(userName, fullName, phone, address, avatar, token);
    }

    @GetMapping("/getAll")
    public List<Users> getAllNonAdminUsers() {
        return userRepository.findAllNonAdminUsers();
    }


    @PostMapping("/lock")
    public ResponseEntity<String> lockUser(@RequestParam(value = "userIds") List<Long> userId) {
        try {
            userService.lockUserAccounts(userId);
            return ResponseEntity.status(HttpStatus.OK).body("200");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("400");
        }
    }

    @PostMapping("/unlock")
    public ResponseEntity<String> unLockUser(@RequestParam(value = "userIds") List<Long> userId) {
        try {
            userService.unLockUserAccounts(userId);
            return ResponseEntity.status(HttpStatus.OK).body("200");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("400");
        }
    }

    @GetMapping("/sortedById")
    public ResponseEntity<List<Users>> getAllUsersSortedById() {
        List<Users> usersSortedById = userRepository.findAllByOrderByIdDesc();
        return ResponseEntity.ok(usersSortedById);
    }
}

