package com.andy.gomoku.entity;

public class ConfTitle extends BaseEntity{

	private static final long serialVersionUID = 1L;

	public ConfTitle() {
	}
	
	public ConfTitle(Long id) {
		super(id);
	}
	
	
	private Integer minScr;
	
	private Integer maxScr;
	
	private Integer titleSort;
	
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTitleSort() {
		return titleSort;
	}

	public void setTitleSort(Integer titleSort) {
		this.titleSort = titleSort;
	}

	public Integer getMinScr() {
		return minScr;
	}

	public void setMinScr(Integer minScr) {
		this.minScr = minScr;
	}

	public Integer getMaxScr() {
		return maxScr;
	}

	public void setMaxScr(Integer maxScr) {
		this.maxScr = maxScr;
	}

	public boolean inScr(Integer scr){
		
		return (scr >= minScr && scr < maxScr);
			
	}

	public static String table() {
		return "conf_title";
	}
	
}
