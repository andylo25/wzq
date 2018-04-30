package com.andy.gomoku.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageVO implements Serializable {
	private static final long serialVersionUID = -3272484150237718632L;

	private int page = 1;

	private int epage= 10;

	private Integer total_items;

	private Integer total_pages;

	private List<Map<String,Object>> items;

	private Object params;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getEpage() {
		return epage;
	}

	public void setEpage(int epage) {
		this.epage = epage;
	}

	public Integer getTotal_items() {
		return total_items;
	}

	public void setTotal_items(Integer total_items) {
		this.total_items = total_items;
		int totalPage = total_items%epage==0 ? total_items/epage : total_items/epage + 1;
		this.setTotal_pages(totalPage);
	}

	public Integer getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(Integer total_pages) {
		this.total_pages = total_pages;
	}

	public List<Map<String,Object>> getItems() {
		return items;
	}

	public void setItems(List<Map<String,Object>> items) {
		this.items = items;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	@Override
    public String toString() {
       StringBuilder builder = new StringBuilder();
       builder.append("Page [")
       		  .append("epage=").append(epage).append(",")
       		  .append("page=").append(page).append(",")
       		  .append("items=").append(items).append(",")
       		  .append("total_pages=").append(total_pages).append(",")
       		  .append("total_items=").append(total_items)
       		  .append("]");
       return builder.toString();
    }
}