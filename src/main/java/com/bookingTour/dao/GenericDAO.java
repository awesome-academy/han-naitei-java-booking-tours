package com.bookingTour.dao;

import com.bookingTour.entity.BaseEntity;
import com.bookingTour.util.SearchQueryTemplate;
import org.hibernate.criterion.Criterion;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public interface GenericDAO<E extends BaseEntity, Id extends Serializable> {

    public E find(Id id) throws Exception;

    public E find(Id id, boolean lock) throws Exception;

    public List<E> findAll() throws Exception;

    public List<E> findByExample(E exampleInstance) throws Exception;

    public List<E> findByExample(E exampleInstance, String[] excludeProperty) throws Exception;

    public int count(E exampleInstance, String[] excludeProperty, boolean isLike) throws Exception;

    public int count() throws Exception;

    public int count(Criterion... criterion) throws Exception;

    public E makePersistent(E entity) throws Exception;

    public void makeTransient(E entity) throws Exception;

    public List<E> findByCriteria(Criterion... criterion) throws Exception;

    public Timestamp getSystemTimestamp() throws Exception;

    public Page<E> find(final SearchQueryTemplate searchQueryTemplate) throws Exception;

}
