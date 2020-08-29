package com.bookingTour.controller;

import com.bookingTour.model.TourDetailInfo;
import com.bookingTour.model.TourInfo;
import com.bookingTour.service.TourDetailService;
import com.bookingTour.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/tours/{tour_id}/tour-details")
public class TourDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(TourDetailsController.class);

    @Autowired
    private TourDetailService tourDetailService;
    @Autowired
    private TourService tourService;

    @GetMapping(value = { "/add" })
    public String add(@PathVariable(name = "tour_id", required = true)  @Min(1) Long tourId, Locale locale, Model model) {
        TourInfo tourInfo = tourService.findTour(tourId);
        if (tourInfo == null){
            model.addAttribute("errorMsg", "Tour Not Found");
            return "templates/error";
        }
        TourDetailInfo tourDetail = new TourDetailInfo();
        tourDetail.setPrice(1d);
        tourDetail.setTour(tourInfo);
        tourDetail.setDuration(tourInfo.getExpectedDurationMin());
        tourDetail.setTourId(tourInfo.getId());
        model.addAttribute("tourDetail", tourDetail);
        return "tour-details/add";
    }

    @PostMapping(value = {"","/"})
    public String create(@ModelAttribute("tourDetail") @Validated TourDetailInfo tourDetailInfo, BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning add tourDetail page, validate failed");
            return "tour-details/add";
        }
        TourDetailInfo tourDetail = tourDetailService.addTourDetail(tourDetailInfo);
        if (tourDetail == null){
            model.addAttribute("errorMsg", "Create Tour Failed");
            return "templates/error";
        }
        return "redirect: " + request.getContextPath() + "/tours/"+ tourDetailInfo.getTourId()+"/tour-details";
    }

    @GetMapping(value = {"","/index"})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page,
                        @PathVariable(name = "tour_id", required = true) Long tourId,
                        Locale locale, Model model, HttpServletRequest request) {
        TourDetailInfo tourDetailInfo = new TourDetailInfo();
        tourDetailInfo.setPage(page.orElse(1));
        tourDetailInfo.setTourId(tourId);
        Page<TourDetailInfo> tourDetails = tourDetailService.paginate(tourDetailInfo);
        model.addAttribute("tourDetails", tourDetails);
        model.addAttribute("tourInfo", tourService.findTour(tourId));
        model.addAttribute("tourDetailSearch", tourDetailInfo);
        return "tour-details/index";
    }

}
