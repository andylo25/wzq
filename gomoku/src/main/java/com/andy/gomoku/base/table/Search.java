package com.andy.gomoku.base.table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 列表页 -- 查询框结构
 */
public class Search implements Serializable {
	private static final long serialVersionUID = 1967564435044525324L;

	private String name;
	
	private String placeholder;
	private String text;
	
	private String type;
	
	private String dateFmt;
	
	private Map<String,String> options;
	
	private Map<String, List<?>> optionMap;
	
	private String[] names;
	
	private String[] texts;
	
	private String[] types;
	
	public Search() {
	}
	
	public Search(String type) {
		this.setType(type);
	}

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
		this.placeholder = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, List<?>> getOptionMap() {
		return optionMap;
	}

	public void setOptionMap(Map<String, List<?>> optionMap) {
		this.optionMap = optionMap;
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

	public String getDateFmt() {
		return dateFmt;
	}

	public void setDateFmt(String dateFmt) {
		this.dateFmt = dateFmt;
	}

	public static Search multiDate(String dateFmt) {
		Search filter = new Search("multi_date");
		filter.setDateFmt(dateFmt);
		return filter;
	}
	public static Search select(Map<String,String> options) {
		Search filter = new Search("select");
		filter.setOptions(options);
		return filter;
	}
	public static Search multiInput() {
		Search filter = new Search("multi_input");
		return filter;
	}
	public static Search input() {
		Search filter = new Search("input");
		return filter;
	}

	public Map<String,String> getOptions() {
		return options;
	}

	public void setOptions(Map<String,String> options) {
		this.options = options;
	}

}