package com.bookingTour.entity;

import java.sql.Timestamp;

public class BookingRequest extends BaseEntity {

    private Long id;
    private Integer status;
    private User user;
    private TourDetail tourDetail;
    private Timestamp startDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TourDetail getTourDetail() {
        return tourDetail;
    }

    public void setTourDetail(TourDetail tourDetail) {
        this.tourDetail = tourDetail;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

}
