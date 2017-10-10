package model;

import java.util.List;

public class InvoiceMaster {

	private int invoice_id;
	private int user_id;
	private String order_type;
	private String user_type;
	private String shiping_address1;
	private String shiping_address2;
	private String shiping_state;
	private String shiping_city;
	private String shiping_postcode;
	private String shiping_additiondetails;
	private String billing_address1;
	private String billing_address2;
	private String billing_state;
	private String billing_city;
	private String billing_postcode;
	private String billing_addtionaldeatails;
	private String create_date;
	private String shiping_fullname;
	private String shiping_email;
	private String billing_fullname;
	private String billing_email;
	private String total_amount;
	private String grand_total;
	private String shipping_charge;
	private String order_no;
	private String ip_address;
	private String order_status;
	private String cart_id;
	private String token;
	private String salt;
	private String shipping_contact_no;
	private String billing_contact_no;
	private List<InvoiceDetails> invoiceProductList;
	
	public List<InvoiceDetails> getInvoiceProductList() {
		return invoiceProductList;
	}
	public void setInvoiceProductList(List<InvoiceDetails> invoiceProductList) {
		this.invoiceProductList = invoiceProductList;
	}
	public int getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(int invoice_id) {
		this.invoice_id = invoice_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getShiping_address1() {
		return shiping_address1;
	}
	public void setShiping_address1(String shiping_address1) {
		this.shiping_address1 = shiping_address1;
	}
	public String getShiping_address2() {
		return shiping_address2;
	}
	public void setShiping_address2(String shiping_address2) {
		this.shiping_address2 = shiping_address2;
	}
	public String getShiping_state() {
		return shiping_state;
	}
	public void setShiping_state(String shiping_state) {
		this.shiping_state = shiping_state;
	}
	public String getShiping_city() {
		return shiping_city;
	}
	public void setShiping_city(String shiping_city) {
		this.shiping_city = shiping_city;
	}
	public String getShiping_postcode() {
		return shiping_postcode;
	}
	public void setShiping_postcode(String shiping_postcode) {
		this.shiping_postcode = shiping_postcode;
	}
	public String getShiping_additiondetails() {
		return shiping_additiondetails;
	}
	public void setShiping_additiondetails(String shiping_additiondetails) {
		this.shiping_additiondetails = shiping_additiondetails;
	}
	public String getBilling_address1() {
		return billing_address1;
	}
	public void setBilling_address1(String billing_address1) {
		this.billing_address1 = billing_address1;
	}
	public String getBilling_address2() {
		return billing_address2;
	}
	public void setBilling_address2(String billing_address2) {
		this.billing_address2 = billing_address2;
	}
	public String getBilling_state() {
		return billing_state;
	}
	public void setBilling_state(String billing_state) {
		this.billing_state = billing_state;
	}
	public String getBilling_city() {
		return billing_city;
	}
	public void setBilling_city(String billing_city) {
		this.billing_city = billing_city;
	}
	public String getBilling_postcode() {
		return billing_postcode;
	}
	public void setBilling_postcode(String billing_postcode) {
		this.billing_postcode = billing_postcode;
	}
	public String getBilling_addtionaldeatails() {
		return billing_addtionaldeatails;
	}
	public void setBilling_addtionaldeatails(String billing_addtionaldeatails) {
		this.billing_addtionaldeatails = billing_addtionaldeatails;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getShiping_fullname() {
		return shiping_fullname;
	}
	public void setShiping_fullname(String shiping_fullname) {
		this.shiping_fullname = shiping_fullname;
	}
	public String getShiping_email() {
		return shiping_email;
	}
	public void setShiping_email(String shiping_email) {
		this.shiping_email = shiping_email;
	}
	public String getBilling_fullname() {
		return billing_fullname;
	}
	public void setBilling_fullname(String billing_fullname) {
		this.billing_fullname = billing_fullname;
	}
	public String getBilling_email() {
		return billing_email;
	}
	public void setBilling_email(String billing_email) {
		this.billing_email = billing_email;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getGrand_total() {
		return grand_total;
	}
	public void setGrand_total(String grand_total) {
		this.grand_total = grand_total;
	}
	public String getShipping_charge() {
		return shipping_charge;
	}
	public void setShipping_charge(String shipping_charge) {
		this.shipping_charge = shipping_charge;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getCart_id() {
		return cart_id;
	}
	public void setCart_id(String cart_id) {
		this.cart_id = cart_id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getShipping_contact_no() {
		return shipping_contact_no;
	}
	public void setShipping_contact_no(String shipping_contact_no) {
		this.shipping_contact_no = shipping_contact_no;
	}
	public String getBilling_contact_no() {
		return billing_contact_no;
	}
	public void setBilling_contact_no(String billing_contact_no) {
		this.billing_contact_no = billing_contact_no;
	}
	
}
