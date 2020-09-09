package com.bookingTour.controller;

import com.bookingTour.entity.BookingRequest;
import com.bookingTour.model.BookingRequestInfo;
import com.bookingTour.model.CustomUserDetails;
import com.bookingTour.model.TourDetailInfo;
import com.bookingTour.model.enu.Role;
import com.bookingTour.model.enu.Status;
import com.bookingTour.service.BookingRequestService;
import com.bookingTour.service.TourDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.*;

@Controller
@RequestMapping("/{userId}/booking-requests")
public class BookingRequestsController {

    private static final Logger logger = LoggerFactory.getLogger(BookingRequestsController.class);

    @Autowired
    private TourDetailService tourDetailService;

    @Autowired
    private BookingRequestService bookingRequestService;

    @GetMapping(value = {""})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page,
                        @PathVariable(name = "userId", required = true) Long userId,
                        Locale locale, Model model, Authentication authentication) {
        if (!isCurrentUser(userId, authentication)) {
            model.addAttribute("errorMsg", "current user invalid");
            return "templates/error";
        }
        BookingRequestInfo bookingRequestInfo = new BookingRequestInfo();
        bookingRequestInfo.setUserId(userId);
        bookingRequestInfo.setPage(page.orElse(1));
        Page<BookingRequestInfo> bookingRequestInfos = bookingRequestService.paginate(bookingRequestInfo);
        model.addAttribute("bookingRequests", bookingRequestInfos);
        model.addAttribute("bookingRequestSearch", new BookingRequestInfo());
        return "booking-requests/index";
    }

    @PostMapping(value = "/search")
    public String search(@ModelAttribute(name = "bookingRequestSearch") BookingRequestInfo bookingRequestSearch,
                         @PathVariable(name = "userId", required = true) Long userId,
                         Model model, Authentication authentication) {
        if (!isCurrentUser(userId, authentication)) {
            model.addAttribute("errorMsg", "current user invalid");
            return "templates/error";
        }
        logger.info("list BookingRequest with search");
        Page<BookingRequestInfo> bookingRequests = bookingRequestService.paginate(bookingRequestSearch);
        model.addAttribute("bookingRequests", bookingRequests);
        model.addAttribute("bookingRequestSearch", bookingRequestSearch);
        return "booking-requests/index";
    }

    @PostMapping(value = {"", "/"})
    public String create(@ModelAttribute(name = "bookingRequest") @Validated BookingRequestInfo bookingRequestInfo,
                         @PathVariable(name = "userId", required = true) Long userId, Authentication authentication,
                         BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (!isCurrentUser(userId, authentication)) {
            model.addAttribute("errorMsg", "current user invalid");
            return "templates/error";
        }
        if (bindingResult.hasErrors()) {
            logger.info("Returning add tour page, validate failed");
            return "tours/add";
        }
        BookingRequestInfo bookingRequest = bookingRequestService.addBookingRequest(bookingRequestInfo);
        if (bookingRequest == null) {
            model.addAttribute("errorMsg", "Create BookingRequestInfo Failed");
            return "templates/error";
        }
        return "redirect: " + request.getContextPath() + "/" + userId + "/booking-requests";
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(@PathVariable(name = "id", required = true) @Min(1) Long id,
                       @PathVariable(name = "userId", required = true) @Min(1) Long userId,
                       Model model, Authentication authentication) {
        if (!isCurrentUser(userId, authentication)) {
            model.addAttribute("errorMsg", "current user invalid");
            return "templates/error";
        }
        if (!isModifiable(id, authentication)) {
            model.addAttribute("errorMsg", "cannot modify this record");
            return "templates/error";
        }
        BookingRequestInfo bookingRequestInfo = bookingRequestService.findBookingRequest(id);
        model.addAttribute("bookingRequest", bookingRequestInfo);
        return "booking-requests/edit";
    }

    @PutMapping(value = "/{id}")
    public String update(@PathVariable(name = "userId", required = true) @Min(1) Long userId,
                         @PathVariable(name = "id", required = true) @Min(1) Long id, Authentication authentication,
                         @ModelAttribute("bookingRequest") @Validated BookingRequestInfo bookingRequestInfo,
                         BindingResult bindingResult, Model model, HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.info("Returning edit bookingRequestInfo page, validate failed");
            return "booking-requests/edit";
        }
        if (!isModifiable(id, authentication)) {
            model.addAttribute("errorMsg", "cannot modify this record");
            return "templates/error";
        }
        BookingRequestInfo bookingRequest = null;
        try {
            bookingRequest = bookingRequestService.editBookingRequest(bookingRequestInfo);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Update bookingRequest Failed");
            return "templates/error";
        }
        return "redirect: " + request.getContextPath() + "/" + userId + "/booking-requests";
    }

    @GetMapping(value = {"/{tour_detail_id}/add"})
    public String add(@PathVariable(name = "userId", required = true) @Min(1) Long userId,
                      @RequestParam(name = "tour_detail_id", required = true) @Min(1) Long tourDetailId,
                      Locale locale, Model model, Authentication authentication) {
        if (!isCurrentUser(userId, authentication)) {
            model.addAttribute("errorMsg", "current user invalid");
            return "templates/error";
        }
        TourDetailInfo tourDetailInfo = tourDetailService.findTourDetail(tourDetailId);
        if (tourDetailInfo == null) {
            model.addAttribute("errorMsg", "tourDetail Not Found");
            return "templates/error";
        }
        BookingRequestInfo bookingRequestInfo = new BookingRequestInfo();
        bookingRequestInfo.setTourDetail(tourDetailInfo);
        bookingRequestInfo.setUserId(userId);
        bookingRequestInfo.setTourDetailId(tourDetailInfo.getId());
        model.addAttribute("bookingRequest", bookingRequestInfo);
        return "booking-requests/add";
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String destroy(@PathVariable(name = "userId", required = true) @Min(1) Long userId,
                          @PathVariable(name = "id", required = true) @Min(1) Long id, Model model, HttpServletRequest request,
                          Authentication authentication) throws Exception {
        if (!isCurrentUser(userId, authentication)) {
            model.addAttribute("errorMsg", "current user invalid");
            return "templates/error";
        }
        BookingRequestInfo bookingRequestInfo = bookingRequestService.findBookingRequest(id);
        if (bookingRequestInfo == null) {
            model.addAttribute("errorMsg", "BookingRequest not found");
            return "templates/error";
        }
        if (!isModifiable(id, authentication)) {
            model.addAttribute("errorMsg", "cannot modify this record");
            return "templates/error";
        }
        logger.info("Deleting tour: " + id);
        bookingRequestService.deleteBookingRequest(new BookingRequestInfo(id));
        return "redirect: " + request.getContextPath() + "/" + userId + "/booking-requests";
    }

    private boolean isCurrentUser(Long userId, Authentication authentication) {
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            if (userDetails.getUser().getId().equals(userId))
                return true;
        }
        return false;
    }

    private boolean isModifiable(Long bookingRequestId, Authentication authentication) {
        if (authentication != null) {
            BookingRequestInfo bookingRequest = bookingRequestService.findBookingRequest(bookingRequestId);
            final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> authorityNames = new ArrayList<String>();
            for (final GrantedAuthority grantedAuthority : authorities) {
                authorityNames.add(grantedAuthority.getAuthority());
            }
            if (authorities.contains(Role.ADMIN.name()) && !bookingRequest.getStatus().equals(Status.CANCELLED))
                return true;
            else {
                if (bookingRequest.getStatus().equals(Status.APPROVED) || bookingRequest.getStatus().equals(Status.REJECTED))
                    return false;
                else
                    return true;
            }
        }
        return false;
    }

}
