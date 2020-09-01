package com.bookingTour.service.imp;

import com.bookingTour.dao.CategoryDAO;
import com.bookingTour.entity.Category;
import com.bookingTour.model.CategoryInfo;
import com.bookingTour.service.CategoryService;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryServiceImp implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImp.class);

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    @Transactional(readOnly = true)
    public CategoryInfo findCategory(Long id) {
        logger.info("Checking the category in the database");
        try {
            Category category = categoryDAO.find(id);
            CategoryInfo categoryInfo = null;
            if (category != null) {
                categoryInfo = new CategoryInfo();
                BeanUtils.copyProperties(category, categoryInfo);
            }
            return categoryInfo;
        } catch (Exception e) {
            logger.error("An error occurred while fetching the category details from the database", e);
            return null;
        }
    }

    @Override
    @Transactional
    public CategoryInfo addCategory(CategoryInfo categoryInfo) throws Exception {
        logger.info("Adding the category in the database");
        try {
            Category category = new Category();
            category.setName(categoryInfo.getName());
            category = categoryDAO.makePersistent(category);
            categoryInfo = new CategoryInfo();
            BeanUtils.copyProperties(category, categoryInfo);
            return categoryInfo;
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("An error occurred while adding the category details to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CategoryInfo editCategory(CategoryInfo categoryInfo) throws Exception {
        logger.info("Updating the category in the database");
        try {
            Category category = categoryDAO.find(categoryInfo.getId(), true);
            if (StringUtils.hasText(categoryInfo.getName())) {
                category.setName(categoryInfo.getName());
            }
            categoryDAO.makePersistent(category);
            return categoryInfo;
        } catch (Exception e) {
            logger.error("An error occurred while updating the category details to the database", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean deleteCategory(CategoryInfo categoryInfo) throws Exception {
        logger.info("Deleting the category in the database");
        try {
            Category category = categoryDAO.find(categoryInfo.getId(), true);
            categoryDAO.makeTransient(category);
            return true;
        } catch (Exception e) {
            logger.error("An error occurred while adding the category details to the database", e);
            throw e;
        }
    }

    @Override
    public List<CategoryInfo> findAll(CategoryInfo categoryInfo) {
        logger.info("Fetching all microposts in the database");
        List<CategoryInfo> categoryInfos = new ArrayList<CategoryInfo>();
        try {
            List<Category> categories = categoryDAO.findAll();
            for (Category category : categories) {
                CategoryInfo model = new CategoryInfo();
                BeanUtils.copyProperties(category, model);
                categoryInfos.add(model);
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching all categories from the database", e);
        }
        return categoryInfos;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryInfo> paginate(CategoryInfo categoryInfo) {
        try {
            String query = buildQuery(categoryInfo);
            Map<String, Object> params = buildParams(categoryInfo);
            Page<Category> categories = categoryDAO.paginate(categoryInfo, query, params);
            logger.info("categories: "+categories.getTotalElements());
            return categories.map(category -> {
                CategoryInfo model = new CategoryInfo();
                BeanUtils.copyProperties(category, model);
                return model;
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public int count(CategoryInfo categoryInfo) {
        logger.info("Counting the category in the database");
        try {
            return categoryDAO.count(Restrictions.eq("name", categoryInfo.getName()));
        } catch (Exception e) {
            logger.error("An error occurred while counting the category details from the database", e);
            return 0;
        }
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    private HashMap<String,Object> buildParams(CategoryInfo condition){
        HashMap<String,Object> params = new HashMap<>();
        if (condition.getName() != null && !condition.getName().isEmpty())
            params.put("name",condition.getName());
        return params;
    }

    private String buildQuery(CategoryInfo condition){
        String query = " from Category c where 1=1 ";
        if (condition.getName() != null && !condition.getName().isEmpty())
            query += " and t.name = :name ";
        return query;
    }
}
