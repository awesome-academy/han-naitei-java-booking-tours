package com.bookingTour.dao;

import com.bookingTour.entity.BookingRequest;
import com.bookingTour.model.BookingRequestInfo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface BookingRequestDAO extends GenericDAO<BookingRequest, Long> {
    public Page<BookingRequest> paginate(BookingRequestInfo bookingRequestInfo, String query, Map<String, Object> params);

}
