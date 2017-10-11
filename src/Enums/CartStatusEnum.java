package Enums;

public enum CartStatusEnum {

	OPEN("open"),
	PLACED("placed"),
	PENDING("Pending"),
	INPROGRESS("inprogress"), 
	SHIPPED("shipped"),
	OUTOFDELIVERY("out_of_delivery"),
	DELIVERED("delivered");
	
	
	private String cartStatus;
	

	public String getStatus() {
		return cartStatus;
	}


	public void setStatus(String cartStatus) {
		this.cartStatus = cartStatus;
	}


	private CartStatusEnum(String cartStatus) {
		this.cartStatus = cartStatus;
	}
	
	
}
