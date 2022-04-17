package com.neutrux.api.NeutruxUsersApi.ui.models.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class BlogElementRequestModel implements Serializable {

	private static final long serialVersionUID = 2524244677214825304L;
	
	@NotNull(message = "Name cannot be null!")
	@Size(min=1, message = "Name should contain at least one character!")
	private String name;
	
	private String description;

	@NotNull(message = "Value cannot be null!")
	@Size(min=1, message = "Value should contain at least one character!")
	private String value;
	
	@NotNull(message = "Position cannot be null!")
	@Positive(message= "Position should be greater than ZERO!")
	private long position;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

}
