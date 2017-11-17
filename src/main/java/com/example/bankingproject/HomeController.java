package com.example.bankingproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/")
    public String showMainPage(Principal p) {
        //Principal allows you to store to name you except user to have store object parameters in repository

        return "homepage";
    }


    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }


    @RequestMapping("/account")
    public String listRooms(Model model) {
        //model.addAttribute("rooms", roomRepository.findAll());
        return "usertransactions";
    }

    @GetMapping("/user")
    public String addRoom(Model model){
        //model.addAttribute("room", new Room());
        return "index";
    }

    @GetMapping("/contact")
    public String showContact(Model model){
        //model.addAttribute("room", new Room());
        return "contact";
    }


}
