package com.bookingTour.util;

import org.hibernate.query.Query;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Map;

public class SearchQueryTemplate {
    private String sql;
    private String countSql;
    private Map<String, Object> parameterMap;
    private Pageable pageable;

    public SearchQueryTemplate() {

    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public <E> void setParameters(Query<E> query) {
        if (parameterMap == null) {
            return;
        }
        for (String key : parameterMap.keySet()) {
            if (parameterMap.get(key) instanceof java.util.Arrays) {
                query.setParameterList(key, (Object[]) parameterMap.get(key));
            } else if (parameterMap.get(key) instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) parameterMap.get(key));
            } else {
                query.setParameter(key, parameterMap.get(key));
            }
        }
    }

    public <E> void setPageable(Query<E> query) {
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

}
