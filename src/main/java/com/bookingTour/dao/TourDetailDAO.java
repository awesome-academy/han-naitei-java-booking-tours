package com.bookingTour.dao;

import com.bookingTour.entity.TourDetail;
import com.bookingTour.model.TourDetailInfo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TourDetailDAO extends GenericDAO<TourDetail, Long> {

    public Page<TourDetail> paginate(TourDetailInfo tourDetailInfo, String query, Map<String, Object> params);

}
