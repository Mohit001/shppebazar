package model;

import java.io.Serializable;

/**
 * Created by msp on 7/9/17.
 */

public class PaymentInfo implements Serializable{

    private String status;
    private String title;
    private String key;
    private String salt;
    private int is_live_mode;

    
    public PaymentInfo() {
		super();
		status = "";
		title = "";
		key = "";
		salt= "";
		is_live_mode = 0;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getIs_live_mode() {
        return is_live_mode;
    }

    public void setIs_live_mode(int is_live_mode) {
        this.is_live_mode = is_live_mode;
    }
}
