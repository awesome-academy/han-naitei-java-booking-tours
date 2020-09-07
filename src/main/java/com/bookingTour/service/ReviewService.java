package com.bookingTour.service;

import com.bookingTour.model.ReviewInfo;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ReviewService {

	public ReviewInfo findReview(Long id);

    public List<ReviewInfo> findAll(ReviewInfo reviewInfo);

    public Page<ReviewInfo> paginate(ReviewInfo reviewInfo);

}
