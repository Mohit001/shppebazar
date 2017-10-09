package model;

import java.util.List;

public class Environment {

	private boolean loginCompalsory;
	private String currency_sign;
	private int currency_id;
	private double currency_multiplier;
	private String token;
	private int user_id;
	private int cart_id;
	private int cartCount;
	private String imagePrefix;
	private String thumbPrefix;
	private PaymentInfo paymentInfo;
	private List<BasicCMS> basicCMSPage;
		
	
	public boolean isLoginCompalsory() {
		return loginCompalsory;
	}
	public void setLoginCompalsory(boolean loginCompalsory) {
		this.loginCompalsory = loginCompalsory;
	}
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
	public String getImagePrefix() {
		return imagePrefix;
	}
	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}
	public String getThumbPrefix() {
		return thumbPrefix;
	}
	public void setThumbPrefix(String thumbPrefix) {
		this.thumbPrefix = thumbPrefix;
	}
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	public int getCartCount() {
		return cartCount;
	}
	public void setCartCount(int cartCount) {
		this.cartCount = cartCount;
	}
	
	
	
}
