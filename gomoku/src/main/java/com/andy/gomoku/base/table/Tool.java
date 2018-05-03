package com.andy.gomoku.base.table;

import java.io.Serializable;
import java.util.List;

/**
 * 列表页 -- 工具条
 */
public class Tool implements Serializable {

	private static final long serialVersionUID = 552819295393268152L;
	
	public static final String IS_CHECK = "1";
	public static final String NOT_CHECK = "-1";

	private String id;
	
	private String click;
	
	private String icon;

	private String single;
	
	private String text;
	
	private String title;

	private String type;

	private String url;

	private String check;

	private String[] ids;
	private String[] clicks;
	private String[] icons;
	private String[] singles;
	private String[] texts;

	private String[] titles;

	private String[] types;

	private String[] urls;

	private String[] checks;
	
	private List<Tool> list;
	
	private String validUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSingle() {
		return single;
	}

	public void setSingle(String single) {
		this.single = single;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.title = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String... ids) {
		this.ids = ids;
	}

	public String[] getClicks() {
		return clicks;
	}

	public void setClicks(String... clicks) {
		this.clicks = clicks;
	}

	public String[] getIcons() {
		return icons;
	}

	public void setIcons(String... icons) {
		this.icons = icons;
	}

	public String[] getSingles() {
		return singles;
	}

	public void setSingles(String... singles) {
		this.singles = singles;
	}

	public String[] getTexts() {
		return texts;
	}

	public void setTexts(String... texts) {
		this.texts = texts;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String... titles) {
		this.titles = titles;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String... types) {
		this.types = types;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String... urls) {
		this.urls = urls;
	}

	public String[] getChecks() {
		return checks;
	}

	public void setChecks(String... checks) {
		this.checks = checks;
	}

	public List<Tool> getList() {
		return list;
	}

	public void setList(List<Tool> list) {
		this.list = list;
	}

    
    public String getValidUrl() {
        return validUrl;
    }

    
    public void setValidUrl(String validUrl) {
        this.validUrl = validUrl;
    }


}