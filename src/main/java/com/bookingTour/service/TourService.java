package com.bookingTour.service;

import com.bookingTour.model.TourInfo;
import org.springframework.data.domain.Page;
import java.util.List;

public interface TourService {

    public TourInfo findTour(Long id);

    public TourInfo addTour(TourInfo tourInfo) throws Exception;

    public TourInfo editTour(TourInfo tourInfo) throws Exception;

    public boolean deleteTour(TourInfo tourInfo) throws Exception;

    public List<TourInfo> findAll(TourInfo tourInfo);

    public Page<TourInfo> paginate(TourInfo tourInfo);

    public int count(TourInfo tourInfo);

}