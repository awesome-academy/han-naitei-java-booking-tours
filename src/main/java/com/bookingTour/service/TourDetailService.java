package com.bookingTour.service;

import com.bookingTour.model.TourDetailInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TourDetailService {

    public TourDetailInfo addTourDetail(TourDetailInfo tourDetailInfo) throws Exception;

    public TourDetailInfo editTourDetail(TourDetailInfo tourDetailInfo) throws Exception;

    public boolean deleteTourDetail(TourDetailInfo tourDetailInfo) throws Exception;

    public List<TourDetailInfo> findAll(TourDetailInfo tourDetailInfo);

    public Page<TourDetailInfo> paginate(TourDetailInfo tourDetailInfo);

    public TourDetailInfo findTourDetail(Long id);

}
