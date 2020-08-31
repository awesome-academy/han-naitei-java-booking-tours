package com.bookingTour.model;

public class CategoryInfo extends BaseModel {

    private Long id;
    private String name;

    public CategoryInfo() {
    }

    public CategoryInfo(Long id) {
        this.id = id;
    }

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
}