package model;

import java.io.Serializable;
import java.sql.Date;

public class Address implements Serializable{
	
	private int address_id;
	private String address1;
	private String address2;
	private String state;
	private String city;
	private String postcode;
	private String addition_detail;
	private String user_id;
	private int is_enable;
	private int default_value;
	private Date create_date;
	private String full_name;
	private String email;
	private String contact_number;
	private boolean selected;


	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public int getAddress_id() {
		return address_id;
	}
	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getAddition_detail() {
		return addition_detail;
	}
	public void setAddition_detail(String addition_detail) {
		this.addition_detail = addition_detail;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(int is_enable) {
		this.is_enable = is_enable;
	}
	public int getDefault_value() {
		return default_value;
	}
	public void setDefault_value(int default_value) {
		this.default_value = default_value;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContact_number() {
		return contact_number;
	}
	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	@Override
	public String toString() {
		return "Address{" +
				"address_id=" + address_id +
				", address1='" + address1 + '\'' +
				", address2='" + address2 + '\'' +
				", state='" + state + '\'' +
				", city='" + city + '\'' +
				", postcode='" + postcode + '\'' +
				", addition_detail='" + addition_detail + '\'' +
				", user_id='" + user_id + '\'' +
				", is_enable=" + is_enable +
				", default_value=" + default_value +
				", create_date=" + create_date +
				", full_name='" + full_name + '\'' +
				", email='" + email + '\'' +
				", contact_number='" + contact_number + '\'' +
				'}';
	}
}
