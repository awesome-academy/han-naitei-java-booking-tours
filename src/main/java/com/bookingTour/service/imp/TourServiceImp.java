package com.bookingTour.service.imp;

import com.bookingTour.dao.CategoryDAO;
import com.bookingTour.dao.TourDAO;
import com.bookingTour.entity.Tour;
import com.bookingTour.model.CategoryInfo;
import com.bookingTour.model.TourInfo;
import com.bookingTour.service.TourService;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourServiceImp implements TourService {

    private static final Logger logger = LoggerFactory.getLogger(TourServiceImp.class);

    private TourDAO tourDAO;
    private CategoryDAO categoryDAO;

    @Override
    public TourInfo findTour(Long id) {
        logger.info("Checking the tour in the database");
        try {
            Tour tour = tourDAO.find(id);
            TourInfo tourInfo = null;
            if (tour != null) {
                tourInfo = new TourInfo();
                BeanUtils.copyProperties(tour, tourInfo);
                CategoryInfo categoryInfo = new CategoryInfo();
                BeanUtils.copyProperties(tour.getCategory(), categoryInfo);
                tourInfo.setCategory(categoryInfo);
            }
            return tourInfo;
        } catch (Exception e) {
            logger.error("An error occurred while fetching the tour details from the database", e);
            return null;
        }
    }

    @Override
    @Transactional
    public TourInfo addTour(TourInfo tourInfo) throws Exception {
        logger.info("Adding the micropost in the database");
        try {
            Tour tour = new Tour();
            tour.setName(tourInfo.getName());
            tour.setDescription(tourInfo.getDescription());
            tour.setCategory(categoryDAO.find(tourInfo.getCategoryId()));
            tour.setStartPoint(tourInfo.getStartPoint());
            tour.setExpectedDurationMin(tourInfo.getExpectedDurationMin());
            tour.setExpectedDurationMax(tourInfo.getExpectedDurationMax());
            tour.setCapacity(tourInfo.getCapacity());
            tour = tourDAO.makePersistent(tour);
            tourInfo = new TourInfo();
            BeanUtils.copyProperties(tour, tourInfo);
            return tourInfo;
        } catch (Exception e) {
            logger.error("An error occurred while adding the micropost details to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public TourInfo editTour(TourInfo tourInfo) throws Exception {
        logger.info("Updating the tour in the database");
        try {
            Tour tour = tourDAO.find(tourInfo.getId(), true);
            if (StringUtils.hasText(tourInfo.getDescription())) {
                tour.setDescription(tourInfo.getDescription());
            }
            if (StringUtils.hasText(tourInfo.getName())) {
                tour.setName(tourInfo.getName());
            }
            if (tourInfo.getCategoryId() != null){
                tour.setCategory(categoryDAO.find(tourInfo.getCategoryId()));
            }
            if (tourInfo.getExpectedDurationMax() != null){
                tour.setExpectedDurationMax(tourInfo.getExpectedDurationMax());
            }
            if (tourInfo.getExpectedDurationMin() != null){
                tour.setExpectedDurationMin(tourInfo.getExpectedDurationMin());
            }
            if (tourInfo.getCapacity() != null){
                tour.setCapacity(tourInfo.getCapacity());
            }
            if (StringUtils.hasText(tourInfo.getStartPoint())) {
                tour.setStartPoint(tourInfo.getStartPoint());
            }
            tourDAO.makePersistent(tour);
            return tourInfo;
        } catch (Exception e) {
            logger.error("An error occurred while updating the tour details to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteTour(TourInfo tourInfo) throws Exception {
        logger.info("Deleting the tour in the database");
        try {
            Tour tour = tourDAO.find(tourInfo.getId(), true);
            tourDAO.makeTransient(tour);
            return true;
        } catch (Exception e) {
            logger.error("An error occurred while deleting the tour details to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourInfo> findAll(TourInfo tourInfo) {
        logger.info("Fetching all tours in the database");
        List<TourInfo> tourInfos = new ArrayList<TourInfo>();
        try {
            Tour condition = new Tour();
            condition.setCategory(categoryDAO.find(tourInfo.getCategoryId()));
            List<Tour> tours = tourDAO.findByExample(condition);
            for (Tour tour : tours) {
                TourInfo model = new TourInfo();
                BeanUtils.copyProperties(tour, model);
                tourInfos.add(model);
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching all tours from the database", e);
        }
        return tourInfos;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TourInfo> paginate(TourInfo tourInfo) {
        try {
            if (tourInfo.getAverageRate() != null && tourInfo.getAverageRate() == 0d)
                tourInfo.setAverageRate(null);
            String query = buildQuery(tourInfo);
            Map<String, Object> params = buildParams(tourInfo);
            Page<Tour> tours = tourDAO.paginate(tourInfo, query, params);
            return tours.map(tour -> {
                TourInfo model = new TourInfo();
                BeanUtils.copyProperties(tour, model);
                CategoryInfo categoryInfo = new CategoryInfo();
                BeanUtils.copyProperties(tour.getCategory(), categoryInfo);
                model.setCategory(categoryInfo);
                return model;
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int count(TourInfo tourInfo) {
        logger.info("Counting the tour in the database");
        try {
            return tourDAO.count(Restrictions.eq("categoryId", tourInfo.getCategoryId()));
        } catch (Exception e) {
            logger.error("An error occurred while counting the tour details from the database", e);
            return 0;
        }
    }

    private HashMap<String,Object> buildParams(TourInfo condition){
        HashMap<String,Object> params = new HashMap<>();
        if (condition.getCategoryId() != null)
            params.put("categoryId",condition.getCategoryId());
        if (condition.getName() != null && !condition.getName().isEmpty())
            params.put("name",condition.getName());
        if (condition.getAverageRate()!= null)
            params.put("averageRate",condition.getAverageRate());
        return params;
    }

    private String buildQuery(TourInfo condition){
        String query = " from Tour t where 1=1 ";
        if (condition.getCategoryId() != null)
            query += " and t.categoryId = :categoryId ";
        if (condition.getName() != null && !condition.getName().isEmpty())
            query += " and t.name = :name ";
        if (condition.getAverageRate()!= null)
            query += " and t.averageRate = :averageRate";
        return query;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void setTourDAO(TourDAO tourDAO) {
        this.tourDAO = tourDAO;
    }

}