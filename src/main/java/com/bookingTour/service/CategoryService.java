package com.bookingTour.service;

import com.bookingTour.model.CategoryInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    public CategoryInfo findCategory(Long id);

    public CategoryInfo addCategory(CategoryInfo CategoryInfo) throws Exception;

    public CategoryInfo editCategory(CategoryInfo CategoryInfo) throws Exception;

    public boolean deleteCategory(CategoryInfo CategoryInfo) throws Exception;

    public List<CategoryInfo> findAll(CategoryInfo CategoryInfo);

    public Page<CategoryInfo> paginate(CategoryInfo CategoryInfo);

    public int count(CategoryInfo CategoryInfo);
}
