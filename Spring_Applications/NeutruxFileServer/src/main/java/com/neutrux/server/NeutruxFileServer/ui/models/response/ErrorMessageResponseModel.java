package com.neutrux.server.NeutruxFileServer.ui.models.response;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ErrorMessageResponseModel {
	
	//Attributes
	private Date timestamp;
	private int status;
	private HttpStatus type;
	private String message;
	
	public ErrorMessageResponseModel(Date timestamp, int status, HttpStatus type, String message) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.type = type;
		this.message = message;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public HttpStatus getType() {
		return type;
	}
	public void setType(HttpStatus type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}