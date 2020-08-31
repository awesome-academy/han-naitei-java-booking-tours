package com.bookingTour.dao;

import com.bookingTour.entity.Category;
import com.bookingTour.model.CategoryInfo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CategoryDAO  extends GenericDAO<Category, Long> {
    public Page<Category> paginate(CategoryInfo categoryInfo, String query, Map<String, Object> params);
}
