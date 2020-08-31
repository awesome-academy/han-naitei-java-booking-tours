package com.bookingTour.dao.imp;

import com.bookingTour.dao.TourDAO;
import com.bookingTour.entity.Tour;
import com.bookingTour.model.TourModel;
import com.bookingTour.util.SearchQueryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class TourDAOImp extends GenericDAOImp<Tour, Long> implements TourDAO {

    private static final Logger logger = LoggerFactory.getLogger(TourDAOImp.class);

    public TourDAOImp() {
        super(Tour.class);
    }

    @Override
    public Page<Tour> paginate(TourModel tour, String query, Map<String, Object> params) {
        String sql = query;
        String countSql = "SELECT COUNT(*) " + sql;
        SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate(sql, countSql, tour.getPageable());
        searchQueryTemplate.addParameters(params);
        searchQueryTemplate.addOrder(Sort.Direction.DESC, "createTime");
        return paginate(searchQueryTemplate);
    }
}