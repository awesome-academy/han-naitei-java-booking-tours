package com.bookingTour.controller;

import com.bookingTour.model.TourModel;
import com.bookingTour.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/tours")
public class ToursController {

    private static final Logger logger = LoggerFactory.getLogger(ToursController.class);

    @Autowired
    private TourService tourService;

    @GetMapping(value = {"","/index"})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale,
                        Model model, HttpServletRequest request) {
        TourModel tourModel = new TourModel();
        tourModel.setPage(page.orElse(1));
        Page<TourModel> tours = tourService.paginate(tourModel);
        model.addAttribute("tourPage", tours);
        model.addAttribute("tourSearch", tourModel);
        return "tours/index";
    }

    @RequestMapping(value = "/search")
    public String search(@ModelAttribute(name = "tourSearch") TourModel tourSearch, Model model) {
        logger.info("list user page with search");
        Page<TourModel> tours = tourService.paginate(tourSearch);
        model.addAttribute("tourPage", tours);
        return "tours/index";
    }

    @GetMapping(value = { "/add" })
    public String add(Locale locale, Model model) {
        model.addAttribute("tour", new TourModel());
        return "tours/add";
    }

    @PostMapping(value = {"","/"})
    public String create(@ModelAttribute("tour") @Validated TourModel tourModel, BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning add tour page, validate failed");
            return "tours/add";
        }
        tourService.addTour(tourModel);
        return "redirect: " + request.getContextPath() + "/tours/index";
    }

    @GetMapping(value = "/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("tour", tourService.findTour(id));
        return "tours/show";
    }

    @GetMapping(value = "{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("tour", tourService.findTour(id));
        return "tours/edit";
    }

    @PutMapping(value = "/{id}")
    public String update(@ModelAttribute("tour") @Validated TourModel tourModel,
                         BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning edit tour page, validate failed");
            return "tours/edit";
        }
        TourModel tour = tourService.editTour(tourModel);
        return "redirect: " + request.getContextPath() + "/tours/" + tour.getId();
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public String destroy(@PathVariable Long id, Model model, HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        logger.info("Deleting tour: " + id);
        tourService.deleteTour(new TourModel(id));
        return "redirect: " + request.getContextPath() + "/tours/index";
    }
}