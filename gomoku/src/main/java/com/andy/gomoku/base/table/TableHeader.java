package com.andy.gomoku.base.table;

import java.io.Serializable;
import java.util.Map;

/**
 * 列表页 -- 表头
 */
public class TableHeader implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String text;
	
	private String type = "";
	
	private Map<String,String> options;
	
	private Search filter;
	
	private String order;
	
	private String[] names;
	
	private String[] texts;
	
	private String[] types;
	
	private String[] filters;
	
	private String[] optionTypes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public String[] getTexts() {
		return texts;
	}

	public void setTexts(String[] texts) {
		this.texts = texts;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public Map<String,String> getOptions() {
		return options;
	}

	public void setOptions(Map<String,String> options) {
		this.options = options;
	}

	public Search getFilter() {
		return filter;
	}

	public void setFilter(Search filter) {
		this.filter = filter;
	}

	public void setFilters(String[] filters) {
		this.filters = filters;
	}
	
	public String[] getFilters() {
		return this.filters;
	}

	public String[] getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(String[] optionTypes) {
		this.optionTypes = optionTypes;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}