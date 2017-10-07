package model;

public class UserCartProduct {

	private int user_cart_product_id;
	private int cart_id;
	private int product_id;
	private String product_name;
	private int product_qty;
	private String product_price;
	private String product_code;
	private int shipping_charge;
	private String status;
	private String gst_type;
	private double gst;
	
	
	public String getGst_type() {
		return gst_type;
	}
	public void setGst_type(String gst_type) {
		this.gst_type = gst_type;
	}
	public double getGst() {
		return gst;
	}
	public void setGst(double gst) {
		this.gst = gst;
	}
	public int getUser_cart_product_id() {
		return user_cart_product_id;
	}
	public void setUser_cart_product_id(int user_cart_product_id) {
		this.user_cart_product_id = user_cart_product_id;
	}
	public int getCart_id() {
		return cart_id;
	}
	public void setCart_id(int cart_id) {
		this.cart_id = cart_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public int getProduct_qty() {
		return product_qty;
	}
	public void setProduct_qty(int product_qty) {
		this.product_qty = product_qty;
	}
	public String getProduct_price() {
		return product_price;
	}
	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public int getShipping_charge() {
		return shipping_charge;
	}
	public void setShipping_charge(int shipping_charge) {
		this.shipping_charge = shipping_charge;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
