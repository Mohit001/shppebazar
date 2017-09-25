package model;

import java.sql.Date;
import java.util.List;

public class Category {

	private int cat_id;
	private String cat_name;
	private String cat_description;
	private String cat_image;
	private int is_enable;
	private int user_id;
	private Date create_date;
	private int parent_id;
	private List<Category> subCategory;
	
	
	
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public String getCat_description() {
		return cat_description;
	}
	public void setCat_description(String cat_description) {
		this.cat_description = cat_description;
	}
	public String getCat_image() {
		return cat_image;
	}
	public void setCat_image(String cat_image) {
		this.cat_image = cat_image;
	}
	public int getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(int is_enable) {
		this.is_enable = is_enable;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public List<Category> getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(List<Category> subCategory) {
		this.subCategory = subCategory;
	}
	@Override
	public String toString() {
		return "Category [cat_id=" + cat_id + ", cat_name=" + cat_name + ", cat_description=" + cat_description
				+ ", cat_image=" + cat_image + ", is_enable=" + is_enable + ", user_id=" + user_id + ", create_date="
				+ create_date + ", parent_id=" + parent_id + ", subCategory=" + subCategory + "]";
	}

	
	
}
