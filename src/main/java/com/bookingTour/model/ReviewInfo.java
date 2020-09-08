package com.bookingTour.model;

public class ReviewInfo extends BaseModel {
	
	private Long id;
	private String content;
	private UserModel user;
	private TourInfo tour;
	private Long userId;
	private Long tourId;
	private String tourName;
	
	public ReviewInfo() {
	}
	
	public ReviewInfo(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public TourInfo getTour() {
		return tour;
	}

	public void setTour(TourInfo tour) {
		this.tour = tour;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTourId() {
		return tourId;
	}

	public void setTourId(Long tourId) {
		this.tourId = tourId;
	}

	public String getTourName() {
		return tourName;
	}

	public void setTourName(String tourName) {
		this.tourName = tourName;
	}
	
}
