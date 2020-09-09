package com.bookingTour.controller;

import com.bookingTour.model.CategoryInfo;
import com.bookingTour.model.TourInfo;
import com.bookingTour.service.CategoryService;
import com.bookingTour.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/tours")
public class ToursController {

    private static final Logger logger = LoggerFactory.getLogger(ToursController.class);

    @Autowired
    private TourService tourService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale,
                        Model model, HttpServletRequest request) {
        TourInfo tourInfo = new TourInfo();
        tourInfo.setPage(page.orElse(1));
        Page<TourInfo> tours = tourService.paginate(tourInfo);
        List<CategoryInfo> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("tours", tours);
        model.addAttribute("tourSearch", tourInfo);
        return "tours/index";
    }

    @RequestMapping(value = "/search")
    public String search(@ModelAttribute(name = "tourSearch") TourInfo tourSearch, Model model) {
        logger.info("list tour page with search");
        Page<TourInfo> tours = tourService.paginate(tourSearch);
        List<CategoryInfo> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("tours", tours);
        return "tours/index";
    }

    @GetMapping(value = "/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("tour", tourService.findTour(id));
        return "tours/show";
    }

}
