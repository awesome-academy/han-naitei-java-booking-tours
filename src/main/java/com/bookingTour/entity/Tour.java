package com.bookingTour.entity;

import java.util.List;

public class Tour extends BaseEntity {

    private Long id;
    private String name;
    private String description;
    private Category category;
    private Double averageRate;
    private List<TourDetail> tourDetails;
    private List<Review> reviews;
    private Integer capacity;
    private String startPoint;
    private Integer expectedDurationMin;
    private Integer expectedDurationMax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public List<TourDetail> getTourDetails() {
        return tourDetails;
    }

    public void setTourDetails(List<TourDetail> tourDetails) {
        this.tourDetails = tourDetails;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public Integer getExpectedDurationMin() {
        return expectedDurationMin;
    }

    public void setExpectedDurationMin(Integer expectedDurationMin) {
        this.expectedDurationMin = expectedDurationMin;
    }

    public Integer getExpectedDurationMax() {
        return expectedDurationMax;
    }

    public void setExpectedDurationMax(Integer expectedDurationMax) {
        this.expectedDurationMax = expectedDurationMax;
    }
}
