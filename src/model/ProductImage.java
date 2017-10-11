package model;

import java.io.Serializable;

public class ProductImage implements Serializable{

	private int imge_id;
	private String image_name;
	private int product_id;
	private String create_date;
	private int is_enable;
	private int isdefault;
	private int user_id;
	private String image_path;
	public int getImge_id() {
		return imge_id;
	}
	public void setImge_id(int imge_id) {
		this.imge_id = imge_id;
	}
	public String getImage_name() {
		return image_name;
	}
	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(int is_enable) {
		this.is_enable = is_enable;
	}
	public int getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getImage_path() {
		return image_path;
	}
	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}
	@Override
	public String toString() {
		return "ProductImage [imge_id=" + imge_id + ", image_name=" + image_name + ", product_id=" + product_id
				+ ", create_date=" + create_date + ", is_enable=" + is_enable + ", isdefault=" + isdefault
				+ ", user_id=" + user_id + ", image_path=" + image_path + "]";
	}
	
	
	
}
