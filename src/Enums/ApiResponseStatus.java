package Enums;

public enum ApiResponseStatus {
	
	// common error
	OUT_OF_SERVICE(101, "Server out of service"),
	INVALID_REQUEST(102, "Invalid request"),
	REQUEST_PARSING_ERROR(103, "Error in parsing request. Please contact to administrator"),
	
	//User related enums
	LOGIN_FAIL(0, "Please provide valid username and password"),
	LOGIN_SUCCESS(1, "User login successfully"),
	MULTIPLE_USER_FOUND(3, "Multiple user record found with same credentials. Please contact to administrator"),
	FORGOT_PASSWORD_FAIL(0, "User not fount with provided email."),
	FORGOT_PASSWORD_SUCCESS(1, "Reset password link sent. Please check your email id"),
	
	
	
	//database related enums
	DATABASE_CONNECTINO_ERROR(1001, "Server encounter issue to connect database."),
	MYSQL_EXCEPTION(1002, "MySQL Exception");
	
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
