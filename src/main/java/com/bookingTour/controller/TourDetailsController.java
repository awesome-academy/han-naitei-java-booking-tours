package com.bookingTour.controller;

import com.bookingTour.model.TourDetailInfo;
import com.bookingTour.service.TourDetailService;
import com.bookingTour.service.TourService;
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
public class TourDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(TourDetailsController.class);

    @Autowired
    private TourDetailService tourDetailService;
    @Autowired
    private TourService tourService;

    @GetMapping(value = {"/tours/{tour_id}/tour-details"})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page,
                        @PathVariable(name = "tour_id", required = true) Long tourId,
                        Locale locale, Model model, HttpServletRequest request) {
        TourDetailInfo tourDetailInfo = new TourDetailInfo();
        tourDetailInfo.setPage(page.orElse(1));
        tourDetailInfo.setTourId(tourId);
        Page<TourDetailInfo> tourDetails = tourDetailService.paginate(tourDetailInfo);
        model.addAttribute("tourDetails", tourDetails);
        tourDetailInfo.setTourId(tourId);
        model.addAttribute("tourInfo", tourService.findTour(tourId));
        model.addAttribute("tourDetailSearch", tourDetailInfo);
        return "tour-details/index";
    }

}
