package com.bookingTour.controller.admin;

import com.bookingTour.model.UserModel;
import com.bookingTour.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller(value = "adminUsersController")
@RequestMapping("/admin/users")
public class UsersController {
    private static final Logger logger = Logger.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    // User
    @GetMapping(value = {"", "/"})
    public String list(Locale locale, Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    @GetMapping(value = "/{id}/delete")
    public String delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttributes) throws Exception {
        UserModel user = userService.findUser(id);
        if (user == null)
            return "templates/error";
        if (userService.deleteUser(user)) {
            redirectAttributes.addFlashAttribute("css", "success");
            redirectAttributes.addFlashAttribute("msg", "Delete user successfully!");
        } else {
            redirectAttributes.addFlashAttribute("css", "error");
            redirectAttributes.addFlashAttribute("msg", "Delete user failed!");
        }
        return "redirect:/admin/users";
    }

}
