package com.neutrux.server.NeutruxChatServer.models;

import java.util.Date;

public class Chat {

	private String message;
	private String username;
	private Date timestamp;

	public Chat(String message, String username, Date timestamp) {
		super();
		this.message = message;
		this.username = username;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Chat [message=" + message + ", username=" + username + ", timestamp=" + timestamp + "]";
	}

}
