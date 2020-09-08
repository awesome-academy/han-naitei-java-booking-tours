package com.bookingTour.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class StaticPagesController {
    private static final Logger logger = Logger.getLogger(StaticPagesController.class);

    @GetMapping(value = {"", "/", "/home"})
    public String home(Locale locale, Model model) {
        logger.info("Home page");
        return "static-pages/home";
    }

    @GetMapping(value = "/about")
    public String about(Locale locale, Model model) {
        logger.info("About page");
        return "static-pages/about";
    }

    @GetMapping(value = "/contact")
    public String contact(Locale locale, Model model) {
        logger.info("Contact page");
        return "static-pages/contact";
    }
}
