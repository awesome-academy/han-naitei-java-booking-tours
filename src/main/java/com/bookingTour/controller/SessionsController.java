package com.bookingTour.controller;


import com.bookingTour.model.CustomUserDetails;
import com.bookingTour.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Locale;

@Controller
public class SessionsController {
    private static final Logger logger = Logger.getLogger(SessionsController.class);

    @GetMapping(value = {"/login", "/users/signin"})
    public String signIn(Locale locale, Model model, Authentication authentication) {
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            if (userDetails.getUser().getId() != null)
                return "redirect:/users/" + userDetails.getUser().getId();
        }
        model.addAttribute("user", new UserModel());
        return "auth/signin";
    }

    @RequestMapping(value = { "/access_denied" }, method = RequestMethod.GET)
    public String accessDenied() {
        logger.info("Access denied");
        return "templates/access_denied";
    }

}
