package com.jwt.vo;

public class User {

	private String role;
	
	private String olmId;
	
	private String password;
	
	private String name;
	
	private long validUpto;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getOlmId() {
		return olmId;
	}

	public void setOlmId(String olmId) {
		this.olmId = olmId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getValidUpto() {
		return validUpto;
	}

	public void setValidUpto(long validUpto) {
		this.validUpto = validUpto;
	}

	@Override
	public String toString() {
		return "User [role=" + role + ", olmId=" + olmId + ", password=" + password + ", name=" + name + ", validTill="
				+ validUpto + "]";
	}
	
}
