package com.andy.gomoku.base.form;

import java.io.Serializable;

public class FormData implements Serializable{
	private static final long serialVersionUID = -5743764757818253572L;

	private String name;
	
	private Object value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}