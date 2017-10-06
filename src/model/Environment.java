package model;

import java.util.List;

public class Environment {

	private String currency_sign;
	private int currency_id;
	private double currency_multiplier;
	private String token;
	private int user_id;
	private int cart_id;
	private List<BasicCMS> basicCMSPage;
	
	
	public String getCurrency_sign() {
		return currency_sign;
	}
	public void setCurrency_sign(String currency_sign) {
		this.currency_sign = currency_sign;
	}
	public int getCurrency_id() {
		return currency_id;
	}
	public void setCurrency_id(int currency_id) {
		this.currency_id = currency_id;
	}
	public double getCurrency_multiplier() {
		return currency_multiplier;
	}
	public void setCurrency_multiplier(double currency_multiplier) {
		this.currency_multiplier = currency_multiplier;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public List<BasicCMS> getBasicCMSPage() {
		return basicCMSPage;
	}
	public void setBasicCMSPage(List<BasicCMS> basicCMSPage) {
		this.basicCMSPage = basicCMSPage;
	}
	public int getCart_id() {
		return cart_id;
	}
	public void setCart_id(int cart_id) {
		this.cart_id = cart_id;
	}
	
	
	
}
