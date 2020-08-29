package com.bookingTour.service;

import com.bookingTour.model.TourDetailInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TourDetailService {

    public TourDetailInfo addTourDetail(TourDetailInfo tourDetailInfo) throws Exception;

}
