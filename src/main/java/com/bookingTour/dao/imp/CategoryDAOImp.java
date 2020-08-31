package com.bookingTour.dao.imp;

import com.bookingTour.dao.CategoryDAO;
import com.bookingTour.entity.Category;
import com.bookingTour.model.CategoryInfo;
import com.bookingTour.util.SearchQueryTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class CategoryDAOImp extends GenericDAOImp<Category, Long> implements CategoryDAO {

    public CategoryDAOImp() {
        super(Category.class);
    }

    @Override
    public Page<Category> paginate(CategoryInfo categoryInfo, String query, Map<String, Object> params) {
        String sql = query;
        String countSql = "SELECT COUNT(*) "+query;
        SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate(sql, countSql, categoryInfo.getPageable());
        searchQueryTemplate.addParameters(params);
        searchQueryTemplate.addOrder(Sort.Direction.DESC, "createTime");
        return paginate(searchQueryTemplate);
    }
}