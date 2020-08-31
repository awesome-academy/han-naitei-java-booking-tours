package com.bookingTour.service;

import com.bookingTour.model.TourModel;
import org.springframework.data.domain.Page;
import java.util.List;

public interface TourService {

    public TourModel findTour(Long id);

    public TourModel addTour(TourModel tourModel) throws Exception;

    public TourModel editTour(TourModel tourModel) throws Exception;

    public boolean deleteTour(TourModel tourModel) throws Exception;

    public List<TourModel> findAll(TourModel tourModel);

    public Page<TourModel> paginate(TourModel tourModel);

    public int count(TourModel tourModel);

}