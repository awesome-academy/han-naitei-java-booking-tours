package com.bookingTour.controller.admin;

import com.bookingTour.model.TourDetailInfo;
import com.bookingTour.model.TourInfo;
import com.bookingTour.service.TourDetailService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.Locale;

@Controller(value = "adminTourDetailsController")
@RequestMapping("/admin/tours/{tour_id}/tour-details")
public class TourDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(TourDetailsController.class);

    @Autowired
    private TourDetailService tourDetailService;
    @Autowired
    private TourService tourService;

    @GetMapping(value = {"/add"})
    public String add(@PathVariable(name = "tour_id", required = true) @Min(1) Long tourId, Locale locale, Model model) {
        TourInfo tourInfo = tourService.findTour(tourId);
        if (tourInfo == null) {
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

    @PostMapping(value = {""})
    public String create(@ModelAttribute("tourDetail") @Validated TourDetailInfo tourDetailInfo, BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning add tourDetail page, validate failed");
            return "tour-details/add";
        }
        TourDetailInfo tourDetail = tourDetailService.addTourDetail(tourDetailInfo);
        if (tourDetail == null) {
            model.addAttribute("errorMsg", "Create Tour Failed");
            return "templates/error";
        }
        return "redirect: " + request.getContextPath() + "/tours/" + tourDetailInfo.getTourId() + "/tour-details";
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(@PathVariable Long id, Model model,
                       @PathVariable(name = "tour_id", required = true) @Min(1) Long tourId) {
        TourDetailInfo tourDetailInfo = tourDetailService.findTourDetail(id);
        if (tourDetailInfo == null)
            return "templates/error";
        model.addAttribute("tourDetail", tourDetailInfo);
        return "tour-details/edit";
    }

    @PutMapping(value = "/{id}")
    public String update(@ModelAttribute("tourDetail") @Validated TourDetailInfo tourDetailInfo,
                         BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning edit tourDetail page, validate failed");
            return "tours/edit";
        }
        TourDetailInfo tourModel = tourDetailService.editTourDetail(tourDetailInfo);
        if (tourModel == null)
            return "templates/error";
        return "redirect: " + request.getContextPath() + "/tours/" + tourDetailInfo.getTourId() + "/tour-details";
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String destroy(@PathVariable Long id, Model model, HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        logger.info("Deleting tour: " + id);
        TourDetailInfo tourDetailInfo = tourDetailService.findTourDetail(id);
        if (tourDetailInfo == null)
            return "templates/error";
        tourDetailService.deleteTourDetail(new TourDetailInfo(id));
        return "redirect: " + request.getContextPath() + "/tours/" + tourDetailInfo.getTourId() + "/tour-details";
    }

}
