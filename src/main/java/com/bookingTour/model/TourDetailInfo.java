package com.bookingTour.model;

public class TourDetailInfo extends BaseModel {

    private Long id;
    private Long tourId;
    private TourInfo tour;
    private Integer duration;
    private Double price;

    public TourDetailInfo() {
    }

    public TourDetailInfo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TourInfo getTour() {
        return tour;
    }

    public void setTour(TourInfo tour) {
        this.tour = tour;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }
}
