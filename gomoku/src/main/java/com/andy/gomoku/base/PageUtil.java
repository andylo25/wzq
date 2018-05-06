package com.andy.gomoku.base;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.andy.gomoku.base.form.FormField;
import com.andy.gomoku.base.table.PageStructure;
import com.andy.gomoku.base.table.Search;
import com.andy.gomoku.base.table.TableHeader;
import com.andy.gomoku.base.table.Tool;
import com.google.common.collect.Lists;

/**
 * 页面工具类
 */
public class PageUtil {
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("######0.00");
	
	/**
	 * 构造查看/编辑页面
	 * @param view
	 * @param subUrl
	 * @param formFieldList
	 * @return
	 */
	public static Map<String, Object> createFormPageStructure(String subUrl, List<FormField> formFieldList) {
		return createFormPageStructure(subUrl, formFieldList,null);
	}
	
	/**
	 * 构造查看/编辑页面
	 * @param view
	 * @param subUrl
	 * @param jumpUrl
	 * @param formFieldList
	 * @param cancelBtn 是否添加取消按钮
	 * @return
	 */
	public static Map<String, Object> createFormPageStructure(String subUrl, List<FormField> formFieldList, Object formData) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> formStruct = new HashMap<String, Object>();
		formStruct.put("submitUrl", subUrl);
		formStruct.put("formlist", formFieldList!=null?formFieldList:Lists.newArrayList());
		data.put("formStruct", formStruct);
		if(formData != null){
			data.put("formData", formData);
		}

		return data;
	}
	
	/**
	 * 构造查看/编辑页面
	 * @param view
	 * @param subUrl
	 * @param jumpUrl
	 * @param names
	 * @param texts
	 * @param types
	 * @return
	 */
	public static Map<String, Object> createFormPageStructure(String subUrl, String[] names, String[] texts, String[] types) {
		List<FormField> formFieldList = new ArrayList<FormField>();
		if(names != null) {
			for(int i=0;i<names.length;i++) {
				FormField formField = new FormField();
				formField.setName(names[i]);
				formField.setText(texts[i]);
				formField.setType(types[i]);
				
				formFieldList.add(formField);
			}
		}
		
		return createFormPageStructure(subUrl, formFieldList);
	}
	
	/**
	 * 构造列表页面
	 * @param url 请求数据的URL
	 * @param rightUrl 侧滑url
	 * @param check
	 * @param tableHeader 表头
	 * @param tool 工具条
	 * @param search 查询框
	 * @return
	 * @throws Exception 
	 */
	public static PageStructure createTablePageStructure(String url,String rightUrl, String check, TableHeader tableHeader,
			Tool tool, Search search) {
		PageStructure structure = createTablePageStructure(url, check, tableHeader, tool, search);
		structure.setRightUrl(rightUrl);
		return structure;
	}
	
	/**
	 * 构造列表页面
	 * 
	 * @param url
	 *            请求数据的URL
	 * @param rightUrl
	 *            侧滑url
	 * @param check
	 * @param tableHeader
	 *            表头
	 * @param tool
	 *            工具条
	 * @param search
	 *            查询框
	 * @param viewId
	 *            侧滑页id数据来源(列名)
	 * @return
	 * @throws Exception
	 */
	public static PageStructure createTablePageStructure(String url, String rightUrl, String check,
			TableHeader tableHeader, Tool tool, Search search, String viewId) {
		PageStructure structure = createTablePageStructure(url, check, tableHeader, tool, search);
		structure.setRightUrl(rightUrl);
		structure.setViewId(viewId);
		return structure;
	}

	/**
	 * 构造列表页面
	 * @param url 请求数据的URL
	 * @param check
	 * @param tableHeader 表头
	 * @param tool 工具条
	 * @param search 查询框
	 * @return
	 * @throws Exception 
	 */
	public static PageStructure createTablePageStructure(String url, String check, TableHeader tableHeader,
			Tool tool, Search search) {
		PageStructure pageStructure = new PageStructure();
		pageStructure.setListUrl(url);
		pageStructure.setCheck(check);
		
		//表头
		boolean hasFilter = false;
		List<TableHeader> tableHeaderList = null;
		if(tableHeader != null && tableHeader.getNames() != null) {
			tableHeaderList = new ArrayList<TableHeader>();
			for(int i=0;i<tableHeader.getNames().length;i++) {
				if(tableHeader.getNames()[i] == null) continue;
				
				TableHeader temp = new TableHeader();
				String name = tableHeader.getNames()[i];
				boolean isOrder = false;
				if(name.charAt(0) == '!'){
					name = name.substring(1);
					isOrder = true;
				}
				int mindx = name.indexOf(':');
				if(mindx > 0){
					temp.setName(name.substring(0, mindx));
					temp.setType(name.substring(mindx+1));
				}else{
					temp.setName(name);
				}
				if(isOrder){
					temp.setOrder(temp.getName());
				}
				String text = tableHeader.getTexts()[i];
				mindx = text.indexOf(':');
				if(mindx > 0){
					temp.setText(text.substring(0, mindx));
//					temp.setOptions(DictUtils.getOptionsMap(text.substring(mindx+1)));
				}else{
					temp.setText(text);
				}
				
				
				if(tableHeader.getTypes() != null && tableHeader.getTypes().length > 0){
					temp.setType(tableHeader.getTypes()[i]);
				}
				
				if(tableHeader.getOptionTypes() != null){
					String op = tableHeader.getOptionTypes()[i];
					if(StringUtils.isNotBlank(op)){
//						temp.setOptions(DictUtils.getOptionsMap(op));
					}
				}
				
				if(tableHeader.getFilters() != null){
					String fi = tableHeader.getFilters()[i];
					Search filter = new Search();
					if(StringUtils.isNotBlank(fi)){
						String[] lter = fi.split(":");
						filter.setType(lter[0]);
						if(lter.length > 1){
							if(lter[0].equals("multi_date")){
								filter.setDateFmt(lter[1]);
							}else{
//								filter.setOptions(DictUtils.getOptionsMap(lter[1]));
							}
						}
						temp.setFilter(filter);
						hasFilter = true;
					}
				}
				
				tableHeaderList.add(temp);
			}
		}
		pageStructure.setTableHeader(tableHeaderList);
		pageStructure.setFilter(hasFilter);
		
		//查询框
		List<Search> searchList = null;
		if(search != null && search.getNames() != null) {
			searchList = new ArrayList<Search>();
			for(int i=0;i<search.getNames().length;i++) {
				Search temp = new Search();
				temp.setName(search.getNames()[i]);
				temp.setType(search.getTypes()[i]);
				temp.setText(search.getTexts()[i]);
				
				searchList.add(temp);
			}
		}
		pageStructure.setSearch(searchList);
		
		//工具条
		List<Tool> toolList = new ArrayList<Tool>();
		if(tool != null && tool.getTexts() != null) {
			for(int i=0;i<tool.getTexts().length;i++) {
				if(tool.getTexts()[i] == null) continue;
				
				Tool temp = new Tool();
				temp.setUrl(tool.getUrls()[i]);
				temp.setText(tool.getTexts()[i]);
				temp.setSingle("1");
				temp.setType(tool.getTypes()[i]);
				if (tool.getChecks() != null && tool.getChecks().length > 0) {
					temp.setCheck(tool.getChecks()[i]);
				}
				toolList.add(temp);
			}
		}else if(tool != null){
			if(tool.getList() == null){
				toolList.add(tool);
			}else{
				toolList = tool.getList();
			}
		}
		pageStructure.setToollist(toolList);
		return pageStructure;
	}
}