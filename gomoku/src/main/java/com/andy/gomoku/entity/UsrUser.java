package com.andy.gomoku.entity;

public class UsrUser extends BaseEntity{

	private static final long serialVersionUID = 1L;

	public UsrUser() {
	}
	
	public UsrUser(Long id) {
		super(id);
	}
	
	private String userName;
	
	private String nickName;
	
	private String role;
	
	private String phone;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
