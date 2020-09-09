package com.bookingTour.controller.admin;

import com.bookingTour.model.CategoryInfo;
import com.bookingTour.model.TourInfo;
import com.bookingTour.service.CategoryService;
import com.bookingTour.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

@Controller(value = "adminToursController")
@RequestMapping("/admin/tours")
public class ToursController {
    private static final Logger logger = LoggerFactory.getLogger(com.bookingTour.controller.ToursController.class);

    @Autowired
    private TourService tourService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/add"})
    public String add(Locale locale, Model model) {
        List<CategoryInfo> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("tour", new TourInfo());
        return "tours/add";
    }

    @PostMapping(value = {"", "/"})
    public String create(@ModelAttribute("tour") @Validated TourInfo tourInfo, BindingResult bindingResult, final RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning add tour page, validate failed");
            return "tours/add";
        }
        TourInfo tour = tourService.addTour(tourInfo);
        if (tour == null) {
            model.addAttribute("errorMsg", "Create Tour Failed");
            return "templates/error";
        }
        return "redirect:/tours/index";
    }

    @GetMapping(value = "{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        List<CategoryInfo> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("tour", tourService.findTour(id));
        return "tours/edit";
    }

    @PutMapping(value = "/{id}")
    public String update(@ModelAttribute("tour") @Validated TourInfo tourInfo,
                         BindingResult bindingResult, Model model, HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.info("Returning edit tour page, validate failed");
            return "tours/edit";
        }
        TourInfo tour = null;
        try {
            tour = tourService.editTour(tourInfo);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Update Tour Failed");
            return "templates/error";
        }
        return "redirect:/tours/" + tour.getId();
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String destroy(@PathVariable Long id, Model model, HttpServletRequest request,
                          HttpServletResponse response, final RedirectAttributes redirectAttributes) throws Exception {
        logger.info("Deleting tour: " + id);
        TourInfo tour = tourService.findTour(id);
        if (tour == null)
            return "templates/error";
        if (tourService.deleteTour(tour)) {
            redirectAttributes.addFlashAttribute("css", "success");
            redirectAttributes.addFlashAttribute("msg", "Delete tour successfully!");
        } else {
            redirectAttributes.addFlashAttribute("css", "error");
            redirectAttributes.addFlashAttribute("msg", "Delete tour failed!");
        }
        return "redirect:/tours/index";
    }
}
