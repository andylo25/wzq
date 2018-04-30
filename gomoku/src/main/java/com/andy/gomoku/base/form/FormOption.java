package com.andy.gomoku.base.form;

import java.io.Serializable;

public class FormOption implements Serializable{

	private static final long serialVersionUID = 1L;

	private String text;
	private Object value;
	private String childmark;
	
	public FormOption(String text,Object value) {
		this.setText(text);
		this.setValue(value);
	}
	
	public FormOption(String text,Object value,String childmark){
		this(text,value);
		this.setChildmark(childmark);
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getChildmark() {
		return childmark;
	}
	public void setChildmark(String childmark) {
		this.childmark = childmark;
	}
	
}
