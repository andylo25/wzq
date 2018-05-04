package com.andy.gomoku.dao.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * 业务表Entity
 * @author ThinkGem
 */
public class GenTable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name; 	// 名称
	private String comments;		// 描述
	private String className;		// 实体类名称

	private List<GenTableColumn> columnList = Lists.newArrayList();	// 表列

	private List<String> pkList; // 当前表主键列表
	
	public GenTable() {
		super();
	}


	public String getName() {
		return StringUtils.lowerCase(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		if(comments != null && comments.charAt(comments.length()-1) == '表'){
			return comments.substring(0, comments.length()-1);
		}
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<String> getPkList() {
		return pkList;
	}

	public void setPkList(List<String> pkList) {
		this.pkList = pkList;
	}


	public List<GenTableColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<GenTableColumn> columnList) {
		this.columnList = columnList;
	}

	/**
	 * 获取列名和说明
	 * @return
	 */
	public String getNameAndComments() {
		return getName() + (comments == null ? "" : "  :  " + comments);
	}
	
	/**
	 * 是否存在create_date列
	 * @return
	 */
	public Boolean getCreateDateExists(){
		for (GenTableColumn c : columnList){
			if ("create_date".equals(c.getName())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否存在update_date列
	 * @return
	 */
	public Boolean getUpdateDateExists(){
		for (GenTableColumn c : columnList){
			if ("update_date".equals(c.getName())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否存在del_flag列
	 * @return
	 */
	public Boolean getDelFlagExists(){
		for (GenTableColumn c : columnList){
			if ("del_flag".equals(c.getName())){
				return true;
			}
		}
		return false;
	}
	
	public GenTableColumn getSearch(){
		GenTableColumn result = columnList.get(0);
		for (GenTableColumn c : columnList){
			if ("String".equals(c.getJavaType())){
				result = c;
				break;
			}
		}
		return result;
	}
	
	public String getListCols(){
		StringBuilder sb = new StringBuilder();
		for (GenTableColumn c : columnList){
			if(c.getIsNotListField()){
				sb.append(",").append(c.getName());
			}
		}
		
		return sb.toString();
	}
	
	public String getCols(){
		StringBuilder sb = new StringBuilder();
		for (GenTableColumn c : columnList){
			if(c.getIsNotBaseField()){
				sb.append(",").append("\"").append(c.getName()).append("\"");
			}
		}
		
		return sb.toString();
	}
	
	public String getColComms(){
		StringBuilder sb = new StringBuilder();
		for (GenTableColumn c : columnList){
			if(c.getIsNotBaseField()){
				sb.append(",").append("\"").append(c.getRawComments()).append("\"");
			}
		}
		
		return sb.toString();
	}
	
}


