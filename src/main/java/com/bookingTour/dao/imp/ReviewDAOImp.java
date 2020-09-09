package com.bookingTour.dao.imp;

import com.bookingTour.entity.Review;
import com.bookingTour.model.ReviewInfo;
import com.bookingTour.dao.ReviewDAO;
import com.bookingTour.util.SearchQueryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class ReviewDAOImp extends GenericDAOImp<Review, Long> implements ReviewDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReviewDAO.class);

    public ReviewDAOImp() {
        super(Review.class);
    }

    @Override
    public Page<Review> paginate(ReviewInfo review, String query, Map<String, Object> params) {
    	String sql = query;
        String countSql = "SELECT COUNT(*) " + sql;
        SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate(sql, countSql, review.getPageable());
        searchQueryTemplate.addParameters(params);
        searchQueryTemplate.addOrder(Sort.Direction.DESC, "createTime");
        return paginate(searchQueryTemplate);
    }
}
