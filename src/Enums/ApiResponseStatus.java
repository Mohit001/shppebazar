package Enums;

public enum ApiResponseStatus {
	
	// STATUS_CODE
	// 0 = FAIL
	// 1 = SUCCESS
	
	
	
	// common error 2001-2099 
	OUT_OF_SERVICE(2001, "Server out of service"),
	INVALID_REQUEST(2002, "Invalid request"),
	REQUEST_PARSING_ERROR(2003, "Error in parsing request. Please contact to administrator"),
	
	//User related enums 101-199
	ENVIRONMENT_SUCCESS(1, "Environment retrive successfully"),
	LOGIN_FAIL(0, "Please provide valid username and password"),
	LOGIN_SUCCESS(1, "User login successfully"),
	MULTIPLE_USER_FOUND(101, "Multiple user record found with same credentials. Please contact to administrator"),
	FORGOT_PASSWORD_FAIL(0, "User not fount with provided email."),
	FORGOT_PASSWORD_SUCCESS(1, "Reset password link sent. Please check your email id"),
	INVALID_USER_ID(102, "Please provide valid user id"),
	PROFILE_FATCH_SUCCESS(1, "User profile fatch successfully"),
	REGISTRATION_FAIL(0, "User already register with same email id. Please try with different one."),
	REGISTRATION_SUCCESS(1, "User register Successfully"),
	UPDATE_PROFILE_FAIL(0, "Fail to update profile please pass valid data"),
	UPDATE_PROFILE_SUCCESS(1, "Profile update Successfully"),

	
	
	//Category-Subcategory related
	CATEGORY_NOT_FOUND(0, "No category found"),
	CATEGORY_LIST(1, "Category list"),
	CATEGORY_PRODUCT_NOT_FOUND(0, "No Product found under this category."),
	CATEGORY_PRODUCT_FOUND(0, "Product list for this category."),
	
	//Product Related
	PRODUCT_NOT_FOUND(0, "Product not found. Please send valid product id"),
	PRODUCT_FOUND(1, "Product Details"),
	
	
	
	// Address Related enums Range 201-299
	ADDRESS_FAIL(0, "No address found."),
	ADDRESS_SUCCESS(1, "Address found successfully"),
	ADDRESS_UPDATE_FAIL(0, "Fail to update address"),
	ADDRESS_UPDATE_SUCCESS(1, "Address update successfully"),
	ADDRESS_DELETE_FAIL(0, "Fail to delete address"),
	ADDRESS_DELETE_SUCCESS(1, "Address delete successfully"),
	ADDRESS_ADD_FAIL(0, "Fail to add address"),
	ADDRESS_ADD_SUCCESS(1, "Address addded successfully"),
	ADDRESS_INVALID_USER_ID(201, "Please provide valid userid"),
	ADDRESS_INVALID_ADDRESS_ID(202, "Please provide valid addressid"),
	
	// cart related enums 3001 - 3099 
	CART_LIST_SUCCESS(1, "All cart items"),
	CART_PRODUCT_ADD_FAIL(0, "Fail to add product in cart"),
	CART_PRODUCT_ADD_SUCCESS(1, "Product added successfully"),
	CART_PRODUCT_UPDATE_FAIL(0, "Fail to update product infromation"),
	CART_PRODUCT_UPDATE_SUCCESS(1, "Product information updated successfully"),
	CART_PRODUCT_REMOVE_FAIL(0, "Fail to remove product from cart. Please provide valid cart_id and product_id"),
	CART_PRODUCT_REMOVE_SUCCESS(1, "Product remove successfully"),
	CART_PRODUCT_OUT_OF_STOCK(3001, "Sorry!!! Selected product is out of stock"),
	CART_MULTIPLE_INSTANCE(3002, "Multiple instance retrive for cart please contact to support team"),
	CART_QUENTITY_EXCEED(3003, "You can not add more than 10 quentity for any product in cart"),
	CART_PRODUCT_MISSING(3004, "Invalid request. Product missing in request"),
	CART_SHIPPING_ADDRESS_UPDATE_FAIL(0, "Fail to set shipping address"),
	CART_SHIPPING_ADDRESS_UPDATE_SUCCESS(1, "Shipping address set successfully"),
	CART_BILLING_ADDRESS_UPDATE_FAIL(0, "Fail to set billing address"),
	CART_BILLING_ADDRESS_UPDATE_SUCCESS(1, "Billing address set successfully"),
	CART_PAYMENT_TYPE_LIST_FAIL(0, "Fail to get payment type"),
	CART_PAYMENT_TYPE_LIST_SUCCESS(1, "Payment types fetch successfully"),
	CART_PAYMENT_TYPE_UPDATE_FAIL(0, "Fail to set payment type"),
	CART_PAYMENT_TYPE_UPDATE_SUCCESS(1, "Payment type set successfully"),
	ORDER_PLACE_FAIL(0, "Fail to place order"),
	ORDER_PLACE_SUCCESS(1, "Order placed successfully"),
	
	
	
	
	
	//database related enums range 1001 - 1099
	DATABASE_CONNECTINO_ERROR(1001, "Server encounter issue to connect database."),
	MYSQL_EXCEPTION(1002, "MySQL Exception");
	
	// enum variables
	private final int status_code;
	private final String status_message;
	
	private ApiResponseStatus(int status_code, String status_message) {
		this.status_code = status_code;
		this.status_message = status_message;
	}

	public int getStatus_code() {
		return status_code;
	}

	public String getStatus_message() {
		return status_message;
	}
	
	
	
	
		
}
