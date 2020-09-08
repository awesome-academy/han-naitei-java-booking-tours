package com.bookingTour.service;

import com.bookingTour.entity.TourDetail;
import com.bookingTour.model.BookingRequestInfo;
import org.springframework.data.domain.Page;

public interface BookingRequestService {

    public BookingRequestInfo findBookingRequest(Long id);

    public BookingRequestInfo addBookingRequest(BookingRequestInfo bookingRequestInfo) throws Exception;

    public BookingRequestInfo editBookingRequest(BookingRequestInfo bookingRequestInfo) throws Exception;

    public boolean deleteBookingRequest(BookingRequestInfo bookingRequestInfo) throws Exception;

    public Page<BookingRequestInfo> paginate(BookingRequestInfo bookingRequestInfo);

    public boolean deleteBookingRequestByTourDetail(TourDetail tourDetail) throws Exception;

}
