package com.bookingTour.dao.imp;

import com.bookingTour.dao.TourDetailDAO;
import com.bookingTour.entity.TourDetail;
import com.bookingTour.model.TourDetailInfo;
import com.bookingTour.util.SearchQueryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class TourDetailDAOImp extends GenericDAOImp<TourDetail, Long> implements TourDetailDAO {

    private static final Logger logger = LoggerFactory.getLogger(TourDetailDAOImp.class);

    public TourDetailDAOImp() {
        super(TourDetail.class);
    }

    @Override
    public Page<TourDetail> paginate(TourDetailInfo tourDetailInfo, String query, Map<String, Object> params) {
        String countSql = "SELECT COUNT(*) " + query;
        SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate(query, countSql, tourDetailInfo.getPageable());
        searchQueryTemplate.addParameters(params);
        searchQueryTemplate.addOrder(Sort.Direction.DESC, "createTime");
        return paginate(searchQueryTemplate);
    }

}
