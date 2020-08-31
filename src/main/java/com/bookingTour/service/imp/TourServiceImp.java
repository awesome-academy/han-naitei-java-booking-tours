package com.bookingTour.service.imp;

import com.bookingTour.dao.CategoryDAO;
import com.bookingTour.dao.TourDAO;
import com.bookingTour.entity.Tour;
import com.bookingTour.model.CategoryModel;
import com.bookingTour.model.TourModel;
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
    public TourModel findTour(Long id) {
        logger.info("Checking the tour in the database");
        try {
            Tour tour = tourDAO.find(id);
            TourModel tourModel = null;
            if (tour != null) {
                tourModel = new TourModel();
                BeanUtils.copyProperties(tour, tourModel);
                CategoryModel categoryModel = new CategoryModel();
                BeanUtils.copyProperties(tour.getCategory(), categoryModel);
                tourModel.setCategory(categoryModel);
            }
            return tourModel;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An error occurred while fetching the tour details from the database", e);
            return null;
        }
    }

    @Override
    @Transactional
    public TourModel addTour(TourModel tourModel) throws Exception {
        logger.info("Adding the micropost in the database");
        try {
            Tour tour = new Tour();
            tour.setName(tourModel.getName());
            tour.setDescription(tourModel.getDescription());
            tour.setCategory(categoryDAO.find(tourModel.getCategoryId()));
            tour.setStartPoint(tourModel.getStartPoint());
            tour.setExpectedDurationMin(tourModel.getExpectedDurationMin());
            tour.setExpectedDurationMax(tourModel.getExpectedDurationMax());
            tour.setCapacity(tourModel.getCapacity());
            tour = tourDAO.makePersistent(tour);
            tourModel = new TourModel();
            BeanUtils.copyProperties(tour, tourModel);
            return tourModel;
        } catch (Exception e) {
            logger.error("An error occurred while adding the micropost details to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public TourModel editTour(TourModel tourModel) throws Exception {
        logger.info("Updating the tour in the database");
        try {
            Tour tour = tourDAO.find(tourModel.getId(), true);
            if (StringUtils.hasText(tourModel.getDescription())) {
                tour.setDescription(tourModel.getDescription());
            }
            if (StringUtils.hasText(tourModel.getName())) {
                tour.setName(tourModel.getName());
            }
            if (tourModel.getCategoryId() != null){
                tour.setCategory(categoryDAO.find(tourModel.getId()));
            }
            if (tourModel.getExpectedDurationMax() != null){
                tour.setExpectedDurationMax(tourModel.getExpectedDurationMax());
            }
            if (tourModel.getExpectedDurationMin() != null){
                tour.setExpectedDurationMin(tourModel.getExpectedDurationMin());
            }
            if (tourModel.getCapacity() != null){
                tour.setCapacity(tourModel.getCapacity());
            }
            if (StringUtils.hasText(tourModel.getStartPoint())) {
                tour.setStartPoint(tourModel.getStartPoint());
            }
            tourDAO.makePersistent(tour);
            return tourModel;
        } catch (Exception e) {
            logger.error("An error occurred while updating the tour details to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteTour(TourModel tourModel) throws Exception {
        logger.info("Deleting the tour in the database");
        try {
            Tour tour = tourDAO.find(tourModel.getId(), true);
            tourDAO.makeTransient(tour);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An error occurred while deleting the tour details to the database", e);
            throw e;
        }
    }

    @Override
    public List<TourModel> findAll(TourModel tourModel) {
        logger.info("Fetching all tours in the database");
        List<TourModel> tourModels = new ArrayList<TourModel>();
        try {
            Tour condition = new Tour();
            condition.setCategory(categoryDAO.find(tourModel.getId()));
            List<Tour> tours = tourDAO.findByExample(condition);
            for (Tour tour : tours) {
                TourModel model = new TourModel();
                BeanUtils.copyProperties(tour, model);
                tourModels.add(model);
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching all tours from the database", e);
        }
        return tourModels;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TourModel> paginate(TourModel tourModel) {
        try {
            if (tourModel.getAverageRate() != null && tourModel.getAverageRate() == 0d)
                tourModel.setAverageRate(null);
            String query = buildQuery(tourModel);
            Map<String, Object> params = buildParams(tourModel);
            Page<Tour> tours = tourDAO.paginate(tourModel, query, params);
            return tours.map(tour -> {
                TourModel model = new TourModel();
                BeanUtils.copyProperties(tour, model);
                CategoryModel categoryModel = new CategoryModel();
                BeanUtils.copyProperties(tour.getCategory(), categoryModel);
                model.setCategory(categoryModel);
                return model;
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int count(TourModel tourModel) {
        logger.info("Counting the tour in the database");
        try {
            return tourDAO.count(Restrictions.eq("categoryId", tourModel.getCategoryId()));
        } catch (Exception e) {
            logger.error("An error occurred while counting the tour details from the database", e);
            return 0;
        }
    }

    public void setTourDAO(TourDAO tourDAO) {
        this.tourDAO = tourDAO;
    }

    private HashMap<String,Object> buildParams(TourModel condition){
        HashMap<String,Object> params = new HashMap<>();
        if (condition.getCategoryId() != null)
            params.put("categoryId",condition.getCategoryId());
        if (condition.getName() != null && !condition.getName().isEmpty())
            params.put("name",condition.getName());
        if (condition.getAverageRate()!= null)
            params.put("averageRate",condition.getAverageRate());
        return params;
    }

    private String buildQuery(TourModel condition){
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
}