package main.java;

public class MyMessage {
	
	private String message = "";
	public MyMessage(String message) {
		setMessage(message);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
