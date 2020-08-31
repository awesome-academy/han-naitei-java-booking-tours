package com.bookingTour.dao;

import com.bookingTour.entity.Tour;
import com.bookingTour.model.TourModel;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TourDAO extends GenericDAO<Tour, Long>{

    public Page<Tour> paginate(TourModel tour, String query, Map<String, Object> params);
}