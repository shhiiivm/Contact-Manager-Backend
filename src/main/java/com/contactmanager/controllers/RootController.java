package com.contactmanager.controllers;


import com.contactmanager.entities.User;
import com.contactmanager.helpers.Helper;
import com.contactmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController
{
    @Autowired
    private UserService userService;


    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        if (authentication == null) return;

        String username = Helper.getEmailOfLoggedInUser(authentication);
        System.out.println("Logged in username: " + username);

        User user = userService.getUserByEmail(username);

        if (user != null) {
            System.out.println(user.getName());
            System.out.println(user.getEmail());
            model.addAttribute("loggedInUser", user);
        } else {
            System.out.println("⚠️ No matching user found for email: " + username);
        }
    }
}
