package com.andy.gomoku.dao;

import java.io.Serializable;

public class NameValue implements Serializable {
	private static final long serialVersionUID = -6489843459647864242L;

	private String name;
	
	private Object value;
	
	private boolean isOr;
	
	private String condition;
	
	private String expression;
	
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

	public boolean isOr() {
		return isOr;
	}

	public NameValue setOr(boolean isOr) {
		this.isOr = isOr;
		return this;
	}
	
	public String getCondition() {
		return condition == null ? "=" : condition;
	}

	public NameValue setCondition(String condition) {
		this.condition = condition;
		return this;
	}

	public String getExpression() {
		return expression;
	}

	public NameValue setExpression(String expression) {
		this.expression = expression;
		return this;
	}

	/**
	 * 设置表达式，只适用于Update操作
	 * @param name 数据库字段名
	 * @param expression 表达式，如account + 1
	 */
	public static NameValue setExpression(String name, String expression) {
		NameValue nameValue = new NameValue();
		nameValue.setName(name);
		nameValue.setExpression(expression);
		
		return nameValue;
	}
	
	public NameValue() {}
	
	/**
	 * @param name 数据库字段名
	 * @param value 值
	 */
	public NameValue(String name, Object value) {
		this(name, value, null);
	}
	
	/**
	 * @param name 数据库字段名
	 * @param value 值
	 * @param condition 条件(=,>,<,in,not in等)
	 */
	public NameValue(String name, Object value, String condition) {
		this.name = name;
		this.value = value;
		this.condition = condition;
	}
	
	/**
	 * @param name 数据库字段名
	 * @param value 值
	 * @param condition 条件(=,>,<,in,not in等)
	 * @param isOr true:or/false:and
	 */
	public NameValue(String name, Object value, String condition, boolean isOr) {
		this.isOr = isOr;
		this.name = name;
		this.value = value;
		this.condition = condition;
	}
}