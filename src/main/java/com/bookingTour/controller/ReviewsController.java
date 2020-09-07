package com.bookingTour.controller;

import com.bookingTour.model.ReviewInfo;
import com.bookingTour.service.ReviewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/reviews")
public class ReviewsController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReviewsController.class);

    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale,
                        Model model, HttpServletRequest request) {
        ReviewInfo reviewInfo = new ReviewInfo();
        reviewInfo.setPage(page.orElse(1));
        Page<ReviewInfo> reviews = reviewService.paginate(reviewInfo);
        model.addAttribute("reviewSearch", reviewInfo);
        model.addAttribute("reviews", reviews);
        return "reviews/index";
    }
    
    @RequestMapping(value = "/search")
    public String search(@ModelAttribute(name = "reviewSearch") ReviewInfo reviewSearch, Model model) {
        logger.info("list review page with search");
        Page<ReviewInfo> reviews = reviewService.paginate(reviewSearch);
        model.addAttribute("reviews", reviews);
        return "reviews/index";
    }
    
}
