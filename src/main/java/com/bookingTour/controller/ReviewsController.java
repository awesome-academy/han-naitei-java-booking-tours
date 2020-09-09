package com.bookingTour.controller;

import com.bookingTour.model.CategoryInfo;
import com.bookingTour.model.CustomUserDetails;
import com.bookingTour.model.ReviewInfo;
import com.bookingTour.model.TourInfo;
import com.bookingTour.service.ReviewService;
import com.bookingTour.service.TourService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/reviews")
public class ReviewsController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReviewsController.class);

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private TourService tourService;

    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale,
                        Model model, HttpServletRequest request) {
        ReviewInfo reviewInfo = new ReviewInfo();
        reviewInfo.setPage(page.orElse(1));
        Page<ReviewInfo> reviews = reviewService.paginate(reviewInfo);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewSearch", reviewInfo);
        return "reviews/index";
    }
    
    @RequestMapping(value = "/search")
    public String search(@ModelAttribute(name = "reviewSearch") ReviewInfo reviewSearch, Model model) {
        logger.info("list review page with search");
        Page<ReviewInfo> reviews = reviewService.paginate(reviewSearch);
        model.addAttribute("reviews", reviews);
        return "reviews/index";
    }
    
    @GetMapping(value = "/{id}")
    public String show(@PathVariable Long id, Model model) {
        ReviewInfo review = reviewService.findReview(id);
    	if (review == null) {
    		model.addAttribute("errorMsg", "Review not found");
			return "templates/error";
		}
        model.addAttribute("review", review);
        return "reviews/show";
    }
    
    @GetMapping(value = {"/{tour_id}/add"})
    public String add(@PathVariable(name = "tour_id", required = true)  @Min(1) Long tourId, Locale locale, Model model, Authentication authentication) {
    	ReviewInfo review = new ReviewInfo();
    	if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            if (userDetails.getUser().getId() != null)
                review.setUserId(userDetails.getUser().getId());
        }
    	TourInfo tour = tourService.findTour(tourId);
    	if (tour == null) {
            model.addAttribute("errorMsg", "Tour not found");
            return "templates/error";
        }
        review.setTourId(tour.getId());
        model.addAttribute("review", review);
        return "reviews/add";
    }
    
    @PostMapping(value = {"", "/"})
    public String create(@ModelAttribute("review") @Validated ReviewInfo reviewInfo, BindingResult bindingResult, final RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning add review page, validate failed");
            return "reviews/add";
        }
        ReviewInfo review = reviewService.addReview(reviewInfo);
        if (review == null) {
            model.addAttribute("errorMsg", "Create Review Failed");
            return "templates/error";
        }
        return "redirect: " + request.getContextPath() + "/reviews";
    }
    
    @GetMapping(value = "{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
    	ReviewInfo review = reviewService.findReview(id);
    	if (review == null) {
    		model.addAttribute("errorMsg", "Review not found");
			return "templates/error";
		}
        model.addAttribute("review", review);
        return "reviews/edit";
    }
    
    @PutMapping(value = "/{id}")
    public String update(@ModelAttribute("review") @Validated ReviewInfo reviewInfo,
                         BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning edit review page, validate failed");
            return "reviews/edit";
        }
        ReviewInfo review = reviewService.editReview(reviewInfo);
        return "redirect: " + request.getContextPath() + "/reviews/" + review.getId();
    }
    
    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public String destroy(@PathVariable Long id, Model model, HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        logger.info("Deleting review: " + id);
        reviewService.deleteReview(new ReviewInfo(id));
        return "redirect: " + request.getContextPath() + "/reviews/index";
    }
}
