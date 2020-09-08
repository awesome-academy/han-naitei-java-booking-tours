package com.bookingTour.controller;

import com.bookingTour.model.CustomUserDetails;
import com.bookingTour.model.UserModel;
import com.bookingTour.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    private UserService userService;

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
        userService.addUser(userModel);
        return "redirect:/users/signin";
    }

    @GetMapping(value = "/{id}")
    public String show(@PathVariable Long id, Model model, Authentication authentication) {
        UserModel user = userService.findUser(id);
        if (user == null)
            return "templates/error";
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            if (userDetails.getUser().getId().equals(id)) {
                model.addAttribute("isCurrentUser", true);
            } else {
                model.addAttribute("isCurrentUser", false);
            }
        }
        model.addAttribute("user", user);
        return "users/show";
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(@PathVariable Long id, Model model, Authentication authentication) {
        UserModel user = userService.findUser(id);
        if (user == null)
            return "templates/error";
        String checkAccess = checkAccess(id, authentication);
        if (checkAccess != null)
            return checkAccess;
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PutMapping(value = "/{id}/edit")
    public String update(@ModelAttribute("user") @Validated UserModel userModel, BindingResult bindingResult, Model model,
                         final RedirectAttributes redirectAttributes, HttpServletRequest request, Authentication authentication) throws Exception {
        logger.info("result" + bindingResult);
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        String checkAccess = checkAccess(userModel.getId(), authentication);
        if (checkAccess != null)
            return checkAccess;
        UserModel user = userService.editUser(userModel);
        return "redirect:/users/" + user.getId();
    }

    @GetMapping(value = "/{id}/password")
    public String changePassword(@PathVariable Long id, Model model, Authentication authentication) {
        UserModel user = userService.findUser(id);
        if (user == null)
            return "templates/error";
        String checkAccess = checkAccess(id, authentication);
        if (checkAccess != null)
            return checkAccess;
        model.addAttribute("user", user);
        return "users/password";
    }

    @PutMapping(value = "/{id}/password")
    public String updatePassword(@ModelAttribute("user") @Validated UserModel userModel, BindingResult bindingResult, Model model,
                                 final RedirectAttributes redirectAttributes, HttpServletRequest request, Authentication authentication) throws Exception {
        logger.info("result" + bindingResult);
        if (bindingResult.hasErrors()) {
            return "users/password";
        }
        String checkAccess = checkAccess(userModel.getId(), authentication);
        if (checkAccess != null)
            return checkAccess;
        UserModel user = userService.changePassword(userModel);
        return "redirect:/users/" + user.getId();
    }

    public String checkAccess(Long id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.getUser().getId().equals(id)) {
            return "templates/access_denied";
        }
        return null;
    }

}
