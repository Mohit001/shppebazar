package Enums;

public enum CartStatusEnum {

	OPEN("open"),
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
