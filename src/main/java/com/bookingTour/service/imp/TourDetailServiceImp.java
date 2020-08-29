package com.bookingTour.service.imp;

import com.bookingTour.dao.TourDAO;
import com.bookingTour.dao.TourDetailDAO;
import com.bookingTour.entity.Tour;
import com.bookingTour.entity.TourDetail;
import com.bookingTour.model.TourDetailInfo;
import com.bookingTour.service.TourDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

public class TourDetailServiceImp implements TourDetailService {

    private static final Logger logger = LoggerFactory.getLogger(TourDetailServiceImp.class);

    private TourDetailDAO tourDetailDAO;
    private TourDAO tourDAO;

    @Override
    @Transactional
    public TourDetailInfo addTourDetail(TourDetailInfo tourDetailInfo) throws Exception {
        logger.info("Adding the tourDetail in the database");
        try {
            TourDetail tourDetail = new TourDetail();
            Tour tour = tourDAO.find(tourDetailInfo.getTourId());
            if (tour == null)
                return null;
            tourDetail.setTour(tour);
            tourDetail.setDuration(tourDetailInfo.getDuration());
            tourDetail.setPrice(tourDetailInfo.getPrice());
            tourDetail = tourDetailDAO.makePersistent(tourDetail);
            tourDetailInfo = new TourDetailInfo();
            BeanUtils.copyProperties(tourDetail, tourDetailInfo);
            return tourDetailInfo;
        } catch (Exception e) {
            logger.error("An error occurred while adding the tourDetail to the database", e);
            throw e;
        }
    }

    public void setTourDetailDAO(TourDetailDAO tourDetailDAO) {
        this.tourDetailDAO = tourDetailDAO;
    }

    public void setTourDAO(TourDAO tourDAO) {
        this.tourDAO = tourDAO;
    }

}
