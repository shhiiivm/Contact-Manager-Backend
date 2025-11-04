package com.contactmanager.controllers;

import com.contactmanager.entities.User;
import com.contactmanager.helpers.Message;
import com.contactmanager.helpers.MessageType;
import com.contactmanager.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session) {
        System.out.println("verify Email");

        User user = userRepo.findByEmailToken(token).orElse(null);

        if (user != null) {

            if (user.getEmailToken().equals(token)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);
                session.setAttribute("message", Message.builder()
                        .type(MessageType.green)
                        .content("You email is verified. Now you can login  ")
                        .build());
                return "success_page";
            }

            return "error_page";


        }


        return "error_page";
    }

}
