package basemodel;

public class BaseResponse <T> extends ApiBaseModel{

	private T info;

	public T getInfo() {
		return info;
	}

	public void setInfo(T info) {
		this.info = info;
	}
	
	
}
