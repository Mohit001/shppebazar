package database;

public class Database {

	/**
	 * 
	 * @author root Login table and column names
	 *
	 */
	public static class Login {
		public static final String TABLE_NAME = "login";
		public static final String USER_ID = "user_id";
		public static final String EMAIL = "email";
		public static final String PASSWORD = "password";
		public static final String IS_ENABLE = "is_enable";
		public static final String ROLE = "role";
		public static final String REFFERENCE_ID = "reffrence_id";

	}

	public static class Profile {
		public static final String TABLE_NAME = "profile";
		public static final String PROFILE_ID = "profie_id";
		public static final String USER_ID = "user_id";
		public static final String ACCOUNT_TYPE = "account_type";
		public static final String COMPANY_NAME = "compnay_name";
		public static final String FIRST_NAME = "fname";
		public static final String LAST_NAME = "lname";
		public static final String STATE = "state";
		public static final String COUNTRY = "country";
		public static final String CITY = "city";
		public static final String STREET_ADDRESS = "street_address";
		public static final String ALTERNET_MOBILE = "alternet_mobile";
		public static final String MOBILE = "mobile";

	}

	public static class CategoryMaster {
		public static final String TABLE_NAME = "category_master";
		public static final String CAT_ID = "cat_id";
		public static final String CAT_NAME = "cat_name";
		public static final String CAT_DESCRIPTION = "cat_description";
		public static final String CAT_IMAGE = "cat_image";
		public static final String IS_ENABLE = "is_enable";
		public static final String USER_ID = "user_id";
		public static final String CREATE_DATE = "create_date";
		public static final String PARENT_ID = "parent_id";
	}
	
	public static class BrandMaster {
		public static final String TABLE_NAME = "brand_master";
		public static final String BRAND_ID = "brand_id";
		public static final String BRAND_NAME = "brand_name";
		public static final String BRAND_DESCRIPTION = "brand_description";
		public static final String IS_ENABLE = "is_enable";
		public static final String USER_ID = "user_id";
		public static final String CREATE_DATE = "create_date";
	}
	
	

	public static class ProductMaster {
		public static final String TABLE_NAME = "product_master";
		public static final String PRO_MST_ID = "pro_mst_id";
		public static final String PRO_NAME = "pro_name";
		public static final String PRO_CODE = "pro_code";
		public static final String PRO_DESCRIPTION = "pro_description";
		public static final String PRO_PRICE = "pro_price";
		public static final String IS_ENABLE = "is_enable";
		public static final String CREATE_DATE = "create_date";
		public static final String CAT_ID = "cat_id";
		public static final String BRAND_ID = "brand_id";
		public static final String USER_ID = "user_id";
		public static final String GST_TYPE = "gst_type";
		public static final String GST = "gst";
		public static final String DISCOUNT_PRICE = "discount_price";
		public static final String PRO_IMAGE = "pro_image";

	}

	public static class ProductImage {
		public static final String TABLE_NAME = "product_image";
		public static final String IMAGE_ID = "imge_id";
		public static final String IMAGE_NAME = "image_name";
		public static final String PRODUCT_ID = "product_id";
		public static final String CREATE_DATE = "create_date";
		public static final String IS_ENABLE = "is_enable";
		public static final String DEFAULT = "default";
		public static final String USER_ID = "user_id";
		public static final String IMAGE_PATH = "image_path";
	}

	public static class UserAddress {
		public static final String TABLE_NAME = "user_address";
		public static final String ADDRESS_ID = "address_id";
		public static final String ADDRESS1 = "address1";
		public static final String ADDRESS2 = "address2";
		public static final String STATE = "state";
		public static final String CITY = "city";
		public static final String POSTCODE = "postcode";
		public static final String ADDITIONAL_DETIALS = "addition_detail";
		public static final String USER_ID = "user_id";
		public static final String IS_ENABLE = "is_enable";
		public static final String DEFAULT_VALUE = "default_value";
		public static final String CREATE_DATE = "create_date";
		public static final String FULL_NAME = "full_name";
		public static final String EMAIL = "email";
		public static final String CONTACT_NUMBER = "contact_number";
	}

	public static class UserCartTable {
		public static final String TABLE_NAME = "user_cart";
		public static final String CART_ID = "cart_id";
		public static final String CREATE_DATE = "create_date";
		public static final String IP_ADDRESS = "ip_address";
		public static final String USER_ID = "user_id";
		public static final String CART_STATUS = "cart_status";
		public static final String CART_TOKEN = "token";
		public static final String CART_SHIPPING_ID = "shipping_address_id";
		public static final String CART_BILLING_ID = "billing_address_id";
		public static final String CART_PAYMENT_TYPE_ID = "payment_type_id";
		public static final String CART_PAYMENT_TYPE_CODE = "payment_type_code";
		public static final String SALT = "salt";
	}

	public static class UserCartProductTable {
		public static final String TABLE_NAME = "user_cart_product";
		public static final String USER_CART_PRODUCT_ID = "user_cart_product_id";
		public static final String CART_ID = "cart_id";
		public static final String PRODUCT_ID = "product_id";
		public static final String PRODUCT_NAME = "product_name";
		public static final String PRO_DESCRIPTION = "pro_description";		
		public static final String PRODUCT_QTY = "product_qty";
		public static final String PRODUCT_PRICE = "product_price";
		public static final String PRODUCT_CODE = "product_code";
		public static final String SHIPPING_CHARGE = "shipping_charge";
		public static final String STATUS = "status";
		public static final String GST_TYPE = "gst_type";
		public static final String GST = "gst";
		public static final String SUBTOTAL = "subtotal";
		public static final String DESCRIPTION = "description";
		public static final String CATEGORY_ID = "cat_id";
		public static final String BRAND_ID = "brand_id";
		public static final String DISCOUNT_PRICE = "discount_price";
		public static final String IMAGE_NAME = "image_name";
		public static final String CATEGORY_NAME = "category_name";
		public static final String BRAND_NAME = "brand_name";
	}

	public static class InvoiceMaster {
		public static final String TABLE_NAME = "invoice_master";
		public static final String INVOICE_ID = "invoice_id";
		public static final String USER_ID = "user_id";
		public static final String ORDER_TYPE = "order_type";
		public static final String USER_TYPE = "user_type";
		public static final String SHIPPING_ADDRESS1 = "shiping_address1";
		public static final String SHIPPING_ADDRESS2 = "shiping_address2";
		public static final String SHIPPING_STATE = "shiping_state";
		public static final String SHIPPING_CITY = "shiping_city";
		public static final String SHIPPING_POSTCODE = "shiping_postcode";
		public static final String SHIPPING_ADDITIONAL_DETAILS = "shiping_additiondetails";
		public static final String BILLING_ADDRESS1 = "billing_address1";
		public static final String BILLING_ADDRESS2 = "billing_address2";
		public static final String BILLING_STATE = "billing_state";
		public static final String BILLING_CITY = "billing_city";
		public static final String BILLING_POSTCODE = "billing_postcode";
		public static final String BILLING_ADDITIONAL_DETAILS = "billing_addtionaldeatails";
		public static final String CREATE_DATE = "create_date";
		public static final String SHIPPING_FULL_NAME = "shiping_fullname";
		public static final String SHIPPING_EMAIL = "shiping_email";
		public static final String BILLING_FULL_NAME = "billing_fullname";
		public static final String BILLING_EMAIL = "billing_email";
		public static final String TOTAL_AMOUNT = "total_amount";
		public static final String GRAND_TOTAL = "grand_total";
		public static final String SHIPPING_CHARGE = "shipping_charge";
		public static final String ORDER_NO = "order_no";
		public static final String IP_ADDRESS = "ip_address";
		public static final String ORDER_STATUS = "order_status";
		public static final String TOKEN = "token";
		public static final String SALT = "salt";
		public static final String SHIPPING_ADDRESS_CONTACT_NO = "shipping_contact_no";
		public static final String BILLING_ADDRESS_CONTACT_NO = "billing_contact_no";
		public static final String CART_ID = "cart_id";
	}
	
	public static class InvoiceDetails{
		public static final String TABLE_NAME = "invoice_details";
		public static final String INVOICE_DETAILS_ID= "invoice_details_id";
		public static final String INVOICE_ID= "invoice_id";
		public static final String PRODUCT_ID= "product_id";
		public static final String PRODUCT_NAME= "product_name";
		public static final String PRODUCT_CODE= "product_code";
		public static final String PRODUCT_DESCRIPTION= "product_description";
		public static final String PRODUCT_PRICE= "product_price";
		public static final String PRODUCT_CAT_ID= "product_cat_id";
		public static final String PRODUCT_BRAND_ID= "product_brand_id";
		public static final String PRODUCT_GST_TYPE= "product_gst_type";
		public static final String PRODUCT_GST= "product_gst";
		public static final String PRODUCT_DISCOUNT_PRICE= "product_discount_price";
		public static final String CREATE_DATE= "create_date";
		public static final String PRODUCT_IMAGE_NAME= "product_image_name";
		public static final String PRODUCT_CATEGORY_NAME= "product_category_name";
		public static final String PRODUCT_BRAND_NAME= "product_brand_name";
		public static final String PRODUCT_QTY= "product_qty";
	}
	
	

}
