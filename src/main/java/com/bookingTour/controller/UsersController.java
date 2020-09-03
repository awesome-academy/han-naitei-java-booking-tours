package com.bookingTour.controller;

import com.bookingTour.model.UserModel;
import com.bookingTour.service.imp.UserServiceImp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(value = "/{id}")
    public String show(@PathVariable Long id, Model model) {
        UserModel user = userServiceImp.findUser(id);
        if (user == null)
            return "error";
        model.addAttribute("user", user);
        return "users/show";
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        UserModel user = userServiceImp.findUser(id);
        if (user == null)
            return "error";
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PutMapping(value = "/{id}/edit")
    public String update(@ModelAttribute("user") @Validated UserModel userModel, BindingResult bindingResult,
                         Model model, final RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        logger.info("result" + bindingResult);
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        UserModel user = userServiceImp.editUser(userModel);
        return "redirect:/users/" + user.getId();
    }

}
