package edu.sbu.chatbot.managedbeans;

import java.io.Serializable;

public class Message implements Serializable {
	private String message;
	private boolean system;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSystem() {
		return system;
	}
	public void setSystem(boolean system) {
		this.system = system;
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Message(String message, boolean system) {
		super();
		this.message = message;
		this.system = system;
	}
	
	
}
