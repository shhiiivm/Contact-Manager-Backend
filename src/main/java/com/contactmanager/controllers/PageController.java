package com.contactmanager.controllers;

import com.contactmanager.entities.User;
import com.contactmanager.forms.UserForm;
import com.contactmanager.helpers.Message;
import com.contactmanager.helpers.MessageType;
import com.contactmanager.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        model.addAttribute("name", "substring technology");
        model.addAttribute("youtube", "code with shivam");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage() {
        System.out.println("about page loading");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage() {

        System.out.println("services page loading");
        return "services";
    }

    @RequestMapping("/contact")
    public String contactPage() {
        System.out.println("Contact page loading");
        return "contact";
    }

    @RequestMapping("/register")
    public String registerPage(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        System.out.println("services page loading");
        return "register";
    }

    @RequestMapping("/login")
    public String loginPage() {
        System.out.println("services page loading");
        return "login";
    }

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@ModelAttribute UserForm userForm, HttpSession session) {
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://assets.leetcode.com/users/avatars/avatar_1688203913.png")
        // .build();

        User user = new User();

        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setProfilePic("https://assets.leetcode.com/users/avatars/avatar_1688203913.png");

        User savedUser = userService.saveUser(user);

        Message message = Message.builder().content("Registration Successfull").type(MessageType.green).build();

        session.setAttribute("message", message);

        return "redirect:/register";
    }

}
