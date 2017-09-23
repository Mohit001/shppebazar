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
}
