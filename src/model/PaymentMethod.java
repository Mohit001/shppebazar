package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by msp on 4/8/16.
 */
public class PaymentMethod implements Serializable {

	private int id;
    private String code;
    private String title;
    private ArrayList<PaymentCCTypes> cc_types;
    private boolean selected;

    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<PaymentCCTypes> getCc_types() {
        return cc_types;
    }

    public void setCc_types(ArrayList<PaymentCCTypes> cc_types) {
        this.cc_types = cc_types;
    }
}
