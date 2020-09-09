package com.bookingTour.service;

import com.bookingTour.model.CategoryInfo;
import com.bookingTour.model.ReviewInfo;
import com.bookingTour.model.TourInfo;

import org.springframework.data.domain.Page;
import java.util.List;

public interface ReviewService {

	public ReviewInfo findReview(Long id);
	
	public ReviewInfo addReview(ReviewInfo reviewInfo) throws Exception;
	
	public ReviewInfo editReview(ReviewInfo ReviewInfo) throws Exception;

    public boolean deleteReview(ReviewInfo ReviewInfo) throws Exception;

    public List<ReviewInfo> findAll(ReviewInfo reviewInfo);

    public Page<ReviewInfo> paginate(ReviewInfo reviewInfo);

}
