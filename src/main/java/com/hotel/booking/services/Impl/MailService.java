package com.hotel.booking.services.Impl;


import com.hotel.booking.models.Users;
import com.hotel.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CodeTmpService codeTmpService;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private UserRepository userRepository;


    public boolean sendMail(String mail){
        Users user = userRepository.findByEmail(mail).orElse(null);
        if(user != null){
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromMail);
            simpleMailMessage.setSubject("You valid code");
            simpleMailMessage.setText(codeTmpService.generateCodeTmp(mail));
            simpleMailMessage.setTo(mail);
            javaMailSender.send(simpleMailMessage);
            return true;
        }
        return false;
    }
}
