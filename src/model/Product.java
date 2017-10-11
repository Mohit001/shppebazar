package model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable{

	private int pro_mst_id;
	private String pro_name;
	private String pro_code;
	private String pro_description;
	private String pro_price;
	private int is_enable;
	private int cat_id;
	private String cat_name;
	private int brand_id;
	private String brand_name;
	private int user_id;
	private String gst_type;
	private int gst;
	private int discount_price;
	private String pro_image;
	private List<ProductImage> productImage;
	
	
	public int getPro_mst_id() {
		return pro_mst_id;
	}
	public void setPro_mst_id(int pro_mst_id) {
		this.pro_mst_id = pro_mst_id;
	}
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}
	public String getPro_code() {
		return pro_code;
	}
	public void setPro_code(String pro_code) {
		this.pro_code = pro_code;
	}
	public String getPro_description() {
		return pro_description;
	}
	public void setPro_description(String pro_description) {
		this.pro_description = pro_description;
	}
	public String getPro_price() {
		return pro_price;
	}
	public void setPro_price(String pro_price) {
		this.pro_price = pro_price;
	}
	public int getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(int is_enable) {
		this.is_enable = is_enable;
	}
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	public int getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getGst_type() {
		return gst_type;
	}
	public void setGst_type(String gst_type) {
		this.gst_type = gst_type;
	}
	public int getGst() {
		return gst;
	}
	public void setGst(int gst) {
		this.gst = gst;
	}
	public int getDiscount_price() {
		return discount_price;
	}
	public void setDiscount_price(int discount_price) {
		this.discount_price = discount_price;
	}
	public String getPro_image() {
		return pro_image;
	}
	public void setPro_image(String pro_image) {
		this.pro_image = pro_image;
	}
		
	public List<ProductImage> getProductImage() {
		return productImage;
	}
	public void setProductImage(List<ProductImage> productImage) {
		this.productImage = productImage;
	}
	
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	
	
	
	
	
}
