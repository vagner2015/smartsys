package br.com.vagner.apirote.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing call back in rest controllers
 * @author vagner
 *
 */
public class CallBack implements Serializable{
	
	private static final long serialVersionUID = -5990121018747635686L;

	public static final String OK_STATUS = "OK";
	public static final String NOK_STATUS = "NOK";
	
	private String status;
	private List<String> messages = new ArrayList<String>();
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
