package com.andy.gomoku.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.andy.gomoku.entity.BaseEntity;

public class QueryItem implements Serializable {

	private static final long serialVersionUID = -6879821231581293682L;

	private Integer page;

	private Integer limit;

	private String fields;

	private String group;

	private String orders;

	private String method;

	private String tableName;
	
	private String pageHeader;

	private String pageFooter;

	private String whereCondition;

	private List<Where> where;

	private Map<String, Object> params;

	private boolean isPreProcess = false;
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPageHeader() {
		return pageHeader;
	}

	public void setPageHeader(String pageHeader) {
		this.pageHeader = pageHeader;
	}

	public String getPageFooter() {
		return pageFooter;
	}

	public void setPageFooter(String pageFooter) {
		this.pageFooter = pageFooter;
	}

	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}

	public List<Where> getWhere() {
		return where;
	}

	public void setWhere(List<Where> where) {
		this.where = where;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void setWhere(Where where) {
		if (this.where == null)
			this.where = new ArrayList<Where>();
		if(where != null){
			this.where.add(where);
		}
	}
	
	public void addWhere(List<Where> where) {
		if (this.where == null)
			this.where = new ArrayList<Where>();
		if(where != null){
			this.where.addAll(where);
		}
	}

	public QueryItem() {
		if (this.where == null) {
			this.where = new ArrayList<Where>();
		}
	};

	public QueryItem(Where where) {
		if (this.where == null) {
			this.where = new ArrayList<Where>();
		}
		this.where.add(where);
	}
	
	public QueryItem(String table) {
		if (this.where == null) {
			this.where = new ArrayList<Where>();
		}
		this.tableName = table;
	}

	public boolean isPreProcess() {
		return isPreProcess;
	}

	public void setPreProcess(boolean isPreProcess) {
		this.isPreProcess = isPreProcess;
	}
	
	public static QueryBuilder builder(String table){
	    return new QueryBuilder(table);
	}
	public static QueryBuilder builder(){
        return new QueryBuilder();
    }
	
	public static class QueryBuilder {

	    private QueryItem queryItem=new QueryItem();
	    
	    public QueryItem build(){
	        return queryItem;
	    }
	    public void getList() {
	    	
	    }
	    
	    public QueryBuilder(){
	    }
	    
	    public QueryBuilder(String table){
	        queryItem.setTableName(table);
	    }
	    
	    public QueryBuilder field(String fields){
	        queryItem.setFields(fields);
	        return this;
	    }
	    
	    public QueryBuilder where(Where where){
	        queryItem.setWhere(where);
	        return this;
	    }
	    
	    public QueryBuilder where(String name,Object value){
	        queryItem.setWhere(Where.eq(name, value));
	        return this;
	    }
	    
	    public QueryBuilder group(String group){
	        queryItem.setGroup(group);
	        return this;
	    }
	    
	    public QueryBuilder orders(String orders){
	        queryItem.setOrders(orders);
	        return this;
	    }
	    
	    public QueryBuilder limit(Integer limit){
	        queryItem.setLimit(limit);
	        return this;
	    }
	    
	    public QueryBuilder page(Integer page){
	        queryItem.setPage(page);
	        return this;
	    }
	    
	}

}