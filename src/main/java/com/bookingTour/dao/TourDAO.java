package com.bookingTour.dao;

import com.bookingTour.entity.Tour;
import com.bookingTour.model.TourInfo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TourDAO extends GenericDAO<Tour, Long> {

    public Page<Tour> paginate(TourInfo tour, String query, Map<String, Object> params);
}
