package com.bookingTour.model;

import java.sql.Timestamp;

public class TourInfo extends BaseModel {

    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private Double averageRate;
    private Timestamp createdTime;
    private CategoryInfo category;
    private Integer capacity;
    private String startPoint;
    private Integer expectedDurationMin;
    private Integer expectedDurationMax;

    public TourInfo(){
    }

    public TourInfo(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public Double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public CategoryInfo getCategory() {
        return category;
    }

    public void setCategory(CategoryInfo category) {
        this.category = category;
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