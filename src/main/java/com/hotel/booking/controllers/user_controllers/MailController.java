package com.hotel.booking.controllers.user_controllers;

import com.hotel.booking.services.Impl.MailService;
import com.hotel.booking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @PostMapping("/send/{mail}")
    public boolean sendMail(@PathVariable String mail){
        return mailService.sendMail(mail);
    }

    @GetMapping("/valid/{mail}/{code}")
    public ResponseEntity<String> forgotPass(@PathVariable String mail, @PathVariable String code){
        return userService.forgotPass(code,mail);
    }
}