package com.bookingTour.dao.imp;

import com.bookingTour.dao.BookingRequestDAO;
import com.bookingTour.entity.BookingRequest;
import com.bookingTour.entity.Tour;
import com.bookingTour.model.BookingRequestInfo;
import com.bookingTour.model.TourInfo;
import com.bookingTour.util.SearchQueryTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class BookingRequestDAOImp extends GenericDAOImp<BookingRequest, Long> implements BookingRequestDAO {

    public BookingRequestDAOImp() {
        super(BookingRequest.class);
    }

    @Override
    public Page<BookingRequest> paginate(BookingRequestInfo bookingRequestInfo, String query, Map<String, Object> params) {
        String sql = query;
        String countSql = "SELECT COUNT(*) " + sql;
        SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate(sql, countSql, bookingRequestInfo.getPageable());
        searchQueryTemplate.addParameters(params);
        searchQueryTemplate.addOrder(Sort.Direction.DESC, "createTime");
        return paginate(searchQueryTemplate);
    }

}
