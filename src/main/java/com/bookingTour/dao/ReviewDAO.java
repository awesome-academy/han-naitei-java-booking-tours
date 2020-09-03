package com.bookingTour.dao;

import com.bookingTour.entity.Review;
import com.bookingTour.model.ReviewInfo;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface ReviewDAO extends GenericDAO<Review, Long> {
	
	public Page<Review> paginate(ReviewInfo tour, String query, Map<String, Object> params);
}
