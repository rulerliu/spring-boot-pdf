package com.itmuch.cloud.entity;

public class User {

	private Long userId;
	private String userName;
	private String userCard;
	
	public User() {
	}
	
	public User(Long userId, String userName, String userCard) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userCard = userCard;
	}


	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserCard() {
		return userCard;
	}
	public void setUserCard(String userCard) {
		this.userCard = userCard;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userCard=" + userCard + "]";
	}
	
}
