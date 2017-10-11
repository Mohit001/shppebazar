package model;

import java.io.Serializable;

public class Person implements Serializable{

	private int user_id;
	private int reffrence_id;
	private String email;
	private String name;
	private String password;
	private int is_enable;
	private Profile profile;
	
	
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(int is_enable) {
		this.is_enable = is_enable;
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
	public int getReffrence_id() {
		return reffrence_id;
	}
	public void setReffrence_id(int reffrence_id) {
		this.reffrence_id = reffrence_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	
	
	
	
}
