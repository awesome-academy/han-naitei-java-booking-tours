package com.bookingTour.service.imp;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.bookingTour.dao.UserDAO;
import com.bookingTour.entity.Review;
import com.bookingTour.entity.Tour;
import com.bookingTour.model.TourInfo;
import com.bookingTour.model.CategoryInfo;
import com.bookingTour.model.ReviewInfo;
import com.bookingTour.model.UserModel;
import com.bookingTour.dao.TourDAO;
import com.bookingTour.dao.ReviewDAO;
import com.bookingTour.service.ReviewService;

public class ReviewServiceImp implements ReviewService {
	private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImp.class);

    private TourDAO tourDAO;
    private UserDAO userDAO;
    private ReviewDAO reviewDAO;
    
    @Override
    public ReviewInfo findReview(Long id) {
        logger.info("Checking the reviews in the database");
        try {
            Review review = reviewDAO.find(id);
            ReviewInfo reviewInfo = null;
            if (review != null) {
                reviewInfo = new ReviewInfo();
                BeanUtils.copyProperties(review, reviewInfo);
                TourInfo tourInfo = new TourInfo();
                UserModel userModel = new UserModel();
                BeanUtils.copyProperties(review.getUser(), userModel);
                BeanUtils.copyProperties(review.getTour(), tourInfo);
                reviewInfo.setUser(userModel);
                reviewInfo.setTour(tourInfo);
            }
            return reviewInfo;
        } catch (Exception e) {
            logger.error("An error occurred while fetching the tour details from the database", e);
            return null;
        }
    }

	@Override
	public List<ReviewInfo> findAll(ReviewInfo reviewInfo) {
		logger.info("Fetching all tours in the database");
        List<ReviewInfo> reviewInfos = new ArrayList<ReviewInfo>();
        try {
            Review condition = new Review();
            condition.setUser(userDAO.find(reviewInfo.getUserId()));
            condition.setTour(tourDAO.find(reviewInfo.getTourId()));
            List<Review> reviews = reviewDAO.findByExample(condition);
            for (Review review : reviews) {
                ReviewInfo model = new ReviewInfo();
                BeanUtils.copyProperties(review, model);
                reviewInfos.add(model);
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching all tours from the database", e);
        }
        return reviewInfos;
	}

	@Override
    @Transactional(readOnly = true)
    public Page<ReviewInfo> paginate(ReviewInfo reviewInfo) {
        try {
            String query = buildQuery(reviewInfo);
            Map<String, Object> params = buildParams(reviewInfo);
            Page<Review> reviews = reviewDAO.paginate(reviewInfo, query, params);
            return reviews.map(review -> {
                ReviewInfo model = new ReviewInfo();
                BeanUtils.copyProperties(review, model);
                UserModel userModel = new UserModel();
                BeanUtils.copyProperties(review.getUser(), userModel);
                TourInfo tourInfo = new TourInfo();
                BeanUtils.copyProperties(review.getTour(), tourInfo);
                model.setUser(userModel);
                model.setTour(tourInfo);
                return model;
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
	
	private HashMap<String,Object> buildParams(ReviewInfo condition){
        HashMap<String,Object> params = new HashMap<>();
        if (condition.getUserId() != null)
            params.put("userId",condition.getUserId());
        if (condition.getTourId() != null)
            params.put("tourId",condition.getTourId());
        if (condition.getContent() != null && StringUtils.isNotEmpty(condition.getContent()))
            params.put("content",condition.getContent());
		if (condition.getTourName() != null && StringUtils.isNotEmpty(condition.getTourName())) 
			 params.put("tourName","%" + condition.getTour().getName() +"%");
        return params;
    }
	
	private String buildQuery(ReviewInfo condition){
        String query = " from Review r where 1=1 ";
        if (condition.getUserId() != null)
            query += " and r.user.id = :userId ";
        if (condition.getTourId() != null)
            query += " and r.tour.id = :tourId ";
        if (condition.getContent() != null && StringUtils.isNotEmpty(condition.getContent()))
            query += " and r.content = :content ";
		if (condition.getTourName() != null && StringUtils.isNotEmpty(condition.getTourName())) 
			query += " and r.tour.name like :tourName";
        return query;
    }
	
	public void setReviewDAO(ReviewDAO reviewDAO) {
		this.reviewDAO = reviewDAO;
	}

	public void setTourDAO(TourDAO tourDAO) {
		this.tourDAO = tourDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
}
