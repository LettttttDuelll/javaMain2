package com.example.laptopshop.config;

import com.example.laptopshop.entity.User;
import com.example.laptopshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalModelAttributeConfig {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public User getCurrentUser(Principal principal) {
        if (principal == null) return null;
        return userService.findByUserName(principal.getName()).orElse(null);
    }
}
