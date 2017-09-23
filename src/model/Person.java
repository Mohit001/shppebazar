package model;

public class Person {

	private int id;
	private int reffrence_id;
	private String email;
	private String name;
	private String password;
	private int is_enable;
	
	
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	
	
}
