package com.neutrux.api.NeutruxBlogsApi.ui.models.response;

import java.util.Date;
import java.util.Set;

import org.springframework.http.HttpStatus;

import com.neutrux.api.NeutruxBlogsApi.ui.models.InvalidField;

public class MethodArgumentNotValidExceptionResponseModel {

	private Date timestamp;
	private int status;
	private HttpStatus type;
	private Set<InvalidField> invalidFields;

	public MethodArgumentNotValidExceptionResponseModel(Date timestamp, int status, HttpStatus type,
			Set<InvalidField> invalidFields) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.type = type;
		this.invalidFields = invalidFields;
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

	public Set<InvalidField> getInvalidFields() {
		return invalidFields;
	}

	public void setInvalidFields(Set<InvalidField> invalidFields) {
		this.invalidFields = invalidFields;
	}

}