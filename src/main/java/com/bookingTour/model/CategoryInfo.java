package com.bookingTour.model;

import java.util.Date;

public class CategoryInfo extends BaseModel {

    private Long id;
    private String name;
    private Date createTime;

    public CategoryInfo() {
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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