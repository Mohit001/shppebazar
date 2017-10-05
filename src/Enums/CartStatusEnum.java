package Enums;

public enum CartStatusEnum {

	OPEN("Open"),
	INPROGRESS("Inprogress"), 
	SHIPPED("Shipped"),
	OUTOFDELIVERY("Out of delivery"),
	DELIVERED("Delivered");
	
	
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
