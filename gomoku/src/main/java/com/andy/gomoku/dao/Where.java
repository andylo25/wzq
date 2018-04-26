package com.andy.gomoku.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Where extends NameValue {
	private static final long serialVersionUID = 8744148340895044297L;
	
	private List<? extends NameValue> and;
	
	public List<? extends NameValue> getAnd() {
		return and;
	}

	public void setAnd(List<? extends NameValue> and) {
		this.and = and;
	}
	
	public Where(){ }
	
	/**
	 * @param name 数据库字段名
	 * @param value 值
	 */
	public Where(String name, Object value) {
		super(name, value);
	}
	
	/**
	 * @param name 数据库字段名
	 * @param value 值
	 * @param condition 类型(=,>,<,in,not in等)
	 */
	public Where(String name, Object value, String condition) {
		super(name, value, condition);
	}

	public Where(List<? extends NameValue> and) {
		super();
		this.and = and;
	}
	
	public static Where eq(String name, Object value) {
		return new Where(name, value, Condition.EQ);
	}

	public static Where notEq(String name, Object value) {
		return new Where(name, value, Condition.NEQ);
	}
	
	public static Where gt(String name, Object value) {
		return new Where(name, value, Condition.GT);
	}
	
	public static Where ge(String name, Object value) {
		return new Where(name, value, Condition.GE);
	}
	
	public static Where lt(String name, Object value) {
		return new Where(name, value, Condition.LT);
	}
	
	public static Where le(String name, Object value) {
		return new Where(name, value, Condition.LE);
	}

	public static Where in(String name, Object value) {
	    if(value instanceof Collection){
	        return in(name, (Collection)value);
	    }else if(value instanceof Object[]){
	        return in(name, (Object[])value);
	    }
		return new Where(name, value, Condition.IN);
	}
	
	public static Where in(String name, Object[] values) {
		return new Where(name, StringUtils.join(values,","), Condition.IN);
	}
	
	public static Where in(String name, Collection<?> values) {
		return new Where(name, StringUtils.join(values,","), Condition.IN);
	}

	public static Where notIn(String name, Object value) {
		return new Where(name, value, Condition.NOT_IN);
	}
	
	public static Where notIn(String name, Object[] values) {
		return new Where(name, StringUtils.join(values,","), Condition.NOT_IN);
	}
	
	public static Where notIn(String name, Collection<?> values) {
		return new Where(name, StringUtils.join(values,","), Condition.NOT_IN);
	}

	public static Where like(String name, Object value) {
		return new Where(name, value, Condition.LIKE);
	}
	
	public static Where likeAll(String name, Object value) {
		return new Where(name, "%" + value, Condition.LIKE);
	}
	
	public static Where isNull(String name) {
		return new Where(name, null, Condition.NULL);
	}

	public static Where notNull(String name) {
		return new Where(name, null, Condition.NOT_NULL);
	}

	public static Where setAndList(List<? extends NameValue> and) {
		return new Where(and);
	}
	
	public static Where expression(String expression, boolean isOr) {
		Where where = new Where(null, null);
		where.setOr(isOr);
		where.setExpression(expression);
		
		return where;
	}

	public static Where between(String name, Object startValue, Object endValue) {
		List<NameValue> ands = new ArrayList<NameValue>();
		if(startValue != null)ands.add(new NameValue(name, startValue, ">="));
		if(endValue != null)ands.add(new NameValue(name, endValue, "<="));
		if(ands.size() > 0) return new Where(ands);
		return null;
	}
}