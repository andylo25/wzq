package com.andy.gomoku.base.table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 列表页 -- 页面结构
 */
public class PageStructure implements Serializable {
	private static final long serialVersionUID = 406270128258960210L;

	private String listUrl;
	
	private String rightUrl;
	private String rightUrl1;
	
	private String check;
	private String viewId;// 侧滑页指定id列
	private boolean filter;
	
	private List<Tool> toollist;
	
	private List<Search> search;
	
	private List<TableHeader> tableHeader;
	
	private String firstTitle;
	
	private Map<String,Object> header_data;
	
	private String rowLinkTitle;
	
	private Map treeData;
	private Map ext;

	public String getListUrl() {
		return listUrl;
	}

	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public List<Tool> getToollist() {
		return toollist;
	}

	public void setToollist(List<Tool> toollist) {
		this.toollist = toollist;
	}

	public List<Search> getSearch() {
		return search;
	}

	public void setSearch(List<Search> search) {
		this.search = search;
	}

	public List<TableHeader> getTableHeader() {
		return tableHeader;
	}

	public void setTableHeader(List<TableHeader> tableHeader) {
		this.tableHeader = tableHeader;
	}

	public String getRightUrl() {
		return rightUrl;
	}

	public void setRightUrl(String rightUrl) {
		this.rightUrl = rightUrl;
	}

	public String getRightUrl1() {
		return rightUrl1;
	}

	public void setRightUrl1(String rightUrl1) {
		this.rightUrl1 = rightUrl1;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public String getRowLinkTitle() {
		return rowLinkTitle;
	}

	public void setRowLinkTitle(String rowLinkTitle) {
		this.rowLinkTitle = rowLinkTitle;
	}

	public String getFirstTitle() {
		return firstTitle;
	}

	public void setFirstTitle(String firstTitle) {
		this.firstTitle = firstTitle;
	}

    
    public Map getTreeData() {
        return treeData;
    }

    
    public void setTreeData(Map treeData) {
        this.treeData = treeData;
    }

    
    public Map<String,Object> getHeader_data() {
        return header_data;
    }

    
    public void setHeader_data(Map<String,Object> header_data) {
        this.header_data = header_data;
    }

	public Map getExt() {
		return ext;
	}

	public void setExt(Map ext) {
		this.ext = ext;
	}
	
	/**
	 * 额外参数
	 * @param objects key1,val1,key2,val2...
	 */
	public void setExt(Object...objects) {
		if(objects != null){
			Map<String, Object> ext = Maps.newHashMap();
			for(int i = 0;i<objects.length/2;i++){
				ext.put(objects[2*i].toString(), objects[2*i+1]);
			}
			this.ext = ext;
		}
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	
}