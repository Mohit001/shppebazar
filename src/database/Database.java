package database;

public class Database {

	/**
	 * 
	 * @author root
	 * Login table and column names
	 *
	 */
	public static class Login{
		public static final String TABLE_NAME = "login";
		public static final String USER_ID = "user_id";
		public static final String EMAIL = "email";
		public static final String PASSWORD = "password";
		public static final String IS_ENABLE = "is_enable";
		public static final String ROLE = "role";
		public static final String REFFERENCE_ID = "reffrence_id";

	}
	
	public static class Profile{
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
	
	
	public static class CategoryMaster{
		public static final String TABLE_NAME = "category_master";
		public static final String  CAT_ID = "cat_id";
		public static final String  CAT_NAME = "cat_name";
		public static final String  CAT_DESCRIPTION = "cat_description";
		public static final String  CAT_IMAGE = "cat_image";
		public static final String  IS_ENABLE = "is_enable";
		public static final String  USER_ID = "user_id";
		public static final String  CREATE_DATE = "create_date";
		public static final String  PARENT_ID = "parent_id";
	}
	
	
	public static class ProductMaster{
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
	
	public static class ProductImage{
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
	
	public static class UserAddress{
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
	}
}
