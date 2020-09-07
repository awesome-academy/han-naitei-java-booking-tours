package com.bookingTour.service.imp;

import com.bookingTour.dao.TourDAO;
import com.bookingTour.dao.TourDetailDAO;
import com.bookingTour.entity.Tour;
import com.bookingTour.entity.TourDetail;
import com.bookingTour.model.TourDetailInfo;
import com.bookingTour.entity.TourDetail;
import com.bookingTour.model.TourDetailInfo;
import com.bookingTour.model.TourInfo;
import com.bookingTour.service.TourDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourDetailServiceImp implements TourDetailService {

    private static final Logger logger = LoggerFactory.getLogger(TourDetailServiceImp.class);

    private TourDetailDAO tourDetailDAO;
    private TourDAO tourDAO;

    @Override
    public TourDetailInfo findTourDetail(Long id) {
        logger.info("Checking the tourDetail in the database");
        try {
            TourDetail tourDetail = tourDetailDAO.find(id);
            TourDetailInfo tourDetailInfo = null;
            if (tourDetail != null) {
                tourDetailInfo = new TourDetailInfo();
                BeanUtils.copyProperties(tourDetail, tourDetailInfo);
                TourInfo tourInfo = new TourInfo();
                BeanUtils.copyProperties(tourDetail.getTour(), tourInfo);
                tourDetailInfo.setTour(tourInfo);
            }
            return tourDetailInfo;
        } catch (Exception e) {
            logger.error("An error occurred while fetching the tour details from the database", e);
            return null;
        }
    }

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

    @Override
    public List<TourDetailInfo> findAll(TourDetailInfo tourDetailInfo) {
        logger.info("Fetching all tourDetail in the database");
        List<TourDetailInfo> tourDetailInfos = new ArrayList<TourDetailInfo>();
        try {
            TourDetail condition = new TourDetail();
            condition.setTour(tourDAO.find(tourDetailInfo.getTourId()));
            List<TourDetail> tourDetails = tourDetailDAO.findByExample(condition);
            for (TourDetail tourDetail : tourDetails) {
                TourDetailInfo model = new TourDetailInfo();
                BeanUtils.copyProperties(tourDetail, model);
                tourDetailInfos.add(model);
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching all tourDetails from the database", e);
        }
        return tourDetailInfos;

    }

    @Override
    @Transactional(readOnly = true)
    public Page<TourDetailInfo> paginate(TourDetailInfo tourDetailInfo) {
        try {
            String query = buildQuery(tourDetailInfo);
            Map<String, Object> params = buildParams(tourDetailInfo);
            Page<TourDetail> toursDetails = tourDetailDAO.paginate(tourDetailInfo, query, params);
            return toursDetails.map(tourDetail -> {
                TourDetailInfo model = new TourDetailInfo();
                BeanUtils.copyProperties(tourDetail, model);
                TourInfo tourInfo = new TourInfo();
                BeanUtils.copyProperties(tourDetail.getTour(), tourInfo);
                model.setTour(tourInfo);
                return model;
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public void setTourDetailDAO(TourDetailDAO tourDetailDAO) {
        this.tourDetailDAO = tourDetailDAO;
    }

    public void setTourDAO(TourDAO tourDAO) {
        this.tourDAO = tourDAO;
    }

    private HashMap<String, Object> buildParams(TourDetailInfo condition) {
        HashMap<String, Object> params = new HashMap<>();
        if (condition.getDuration() != null)
            params.put("duration", condition.getDuration());
        if (condition.getPrice() != null)
            params.put("price", condition.getPrice());
        if (condition.getTourId() != null)
            params.put("tourId", condition.getTourId());
        return params;
    }

    private String buildQuery(TourDetailInfo condition) {
        String query = " from TourDetail t where 1=1 ";
        if (condition.getDuration() != null)
            query += " and t.duration = :duration ";
        if (condition.getPrice() != null)
            query += " and t.price = :price ";
        if (condition.getTourId() != null)
            query += " and t.tour.id = :tourId";
        return query;
    }

}
