package com.bookingTour.model;

import com.bookingTour.model.enu.Status;

import java.sql.Date;

public class BookingRequestInfo extends BaseModel {

    private Long id;
    private Long tourDetailId;
    private Long userId;
    private TourDetailInfo tourDetail;
    private Date startDate;
    private UserModel user;
    private Status status;
    private String tourName;
    private String userName;

    public BookingRequestInfo() {

    }

    public BookingRequestInfo(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTourDetailId() {
        return tourDetailId;
    }

    public void setTourDetailId(Long tourDetailId) {
        this.tourDetailId = tourDetailId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TourDetailInfo getTourDetail() {
        return tourDetail;
    }

    public void setTourDetail(TourDetailInfo tourDetail) {
        this.tourDetail = tourDetail;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
