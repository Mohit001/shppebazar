package model;

import java.util.List;

public class UserCart {
	
	private int cart_id;
	private String create_date;
	private String ip_address;
	private int user_id;
	private String cart_status;
	private String token;
	private int shipping_address_id;
	private int billing_address_id;
	private int payment_type_id;
	private String salt;
	private List<UserCartProduct> userCartProduct;
	
	public int getCart_id() {
		return cart_id;
	}
	public void setCart_id(int cart_id) {
		this.cart_id = cart_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public String getCart_status() {
		return cart_status;
	}
	public void setCart_status(String cart_status) {
		this.cart_status = cart_status;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getShipping_address_id() {
		return shipping_address_id;
	}
	public void setShipping_address_id(int shipping_address_id) {
		this.shipping_address_id = shipping_address_id;
	}
	public int getBilling_address_id() {
		return billing_address_id;
	}
	public void setBilling_address_id(int billing_address_id) {
		this.billing_address_id = billing_address_id;
	}
	public int getPayment_type_id() {
		return payment_type_id;
	}
	public void setPayment_type_id(int payment_type_id) {
		this.payment_type_id = payment_type_id;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public List<UserCartProduct> getUserCartProduct() {
		return userCartProduct;
	}
	public void setUserCartProduct(List<UserCartProduct> userCartProduct) {
		this.userCartProduct = userCartProduct;
	}
	
	
	
}
