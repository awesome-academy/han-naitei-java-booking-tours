package com.bookingTour.controller;

import com.bookingTour.model.UserModel;
import com.bookingTour.service.imp.UserServiceImp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping("/users")
public class UsersController {
    private static final Logger logger = Logger.getLogger(UsersController.class);

    @Autowired
    private UserServiceImp userServiceImp;

    @GetMapping(value = "/signup")
    public String add(Locale locale, Model model) {
        model.addAttribute("user", new UserModel());
        return "auth/signup";
    }

    @PostMapping(value = "/signup")
    public String create(@ModelAttribute("user") @Validated UserModel userModel, BindingResult bindingResult,
                         Model model, final RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }
        userServiceImp.addUser(userModel);
        return "redirect:/users/signin";
    }
}
