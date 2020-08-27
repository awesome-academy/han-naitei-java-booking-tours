package com.bookingTour.controller;


import com.bookingTour.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/users")
public class SessionsController {
    private static final Logger logger = Logger.getLogger(SessionsController.class);

    @GetMapping(value = "/signin")
    public String signIn(Locale locale, Model model) {
        model.addAttribute("user", new UserModel());
        return "auth/signin";
    }
}
