package com.bookingTour.controller.admin;

import com.bookingTour.model.BookingRequestInfo;
import com.bookingTour.model.enu.Role;
import com.bookingTour.model.enu.Status;
import com.bookingTour.service.BookingRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.*;

@Controller(value = "adminBookingRequestsController")
@RequestMapping("/admin/booking-requests")
public class BookingRequestsController {

    private static final Logger logger = LoggerFactory.getLogger(BookingRequestsController.class);

    @Autowired
    private BookingRequestService bookingRequestService;

    @GetMapping(value = {""})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page,
                        Locale locale, Model model) {
        BookingRequestInfo bookingRequestInfo = new BookingRequestInfo();
        bookingRequestInfo.setPage(page.orElse(1));
        Page<BookingRequestInfo> bookingRequestInfos = bookingRequestService.paginate(bookingRequestInfo);
        model.addAttribute("bookingRequests", bookingRequestInfos);
        model.addAttribute("bookingRequestSearch", new BookingRequestInfo());
        return "booking-requests/index";
    }

    @PostMapping(value = "/search")
    public String search(@ModelAttribute(name = "bookingRequestSearch") BookingRequestInfo bookingRequestSearch, Model model) {
        logger.info("list BookingRequest with search");
        Page<BookingRequestInfo> bookingRequests = bookingRequestService.paginate(bookingRequestSearch);
        model.addAttribute("bookingRequests", bookingRequests);
        model.addAttribute("bookingRequestSearch", bookingRequestSearch);
        return "booking-requests/index";
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(@PathVariable(name = "id", required = true) @Min(1) Long id,
                       Model model, Authentication authentication) {
        BookingRequestInfo bookingRequestInfo = bookingRequestService.findBookingRequest(id);
        model.addAttribute("bookingRequest", bookingRequestInfo);
        return "booking-requests/edit";
    }

    @PutMapping(value = "/{id}")
    public String update(@PathVariable(name = "id") Long id, Authentication authentication,
                         @ModelAttribute("bookingRequest") @Validated BookingRequestInfo bookingRequestInfo,
                         BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (!isModifiable(id, authentication)) {
            model.addAttribute("errorMsg", "cannot modify this record");
            return "templates/error";
        }
        if (bindingResult.hasErrors()) {
            logger.info("Returning edit bookingRequestInfo page, validate failed");
            return "booking-requests/edit";
        }
        BookingRequestInfo bookingRequest = null;
        try {
            bookingRequest = bookingRequestService.editBookingRequest(bookingRequestInfo);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Update bookingRequest Failed");
            return "templates/error";
        }
        return "redirect: " + request.getContextPath() + "/admin" + "/booking-requests";
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
