package com.andy.gomoku.utils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.andy.gomoku.dao.DaoUtils;
import com.andy.gomoku.entity.BaseEntity;

/**
 * 实体类元数据，主要用于ibatis隐射
 * @author cuiwm
 *
 */
public class EntityMetadata implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Class<? extends BaseEntity> clasz;
	// key=field
	private Map<String,MetadataEntry> fieldCol;
	
	// 查询字段串
	private String fieldSelect;
	
	public EntityMetadata(Class<? extends BaseEntity> clasz) {
		this.clasz = clasz;
	}
	
	public static class MetadataEntry{
		private String fieldName;
		private String colName;
		
		public MetadataEntry(String fieldName,String colName) {
			this.setFieldName(fieldName);
			this.setColName(colName);
		}
		
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getColName() {
			return colName;
		}
		public void setColName(String colName) {
			this.colName = colName;
		}
		
	}
	
	public MetadataEntry getFieldMeta(String fieldName){
		return getFieldCols().get(fieldName);
	}
	
	public Map<String,MetadataEntry> getFieldCols(){
		if(fieldCol == null){
			synchronized (this) {
				if(fieldCol == null){
					buildFieldCol();
				}
			}
		}
		return fieldCol;
	}
	
	private void buildFieldCol() {
		fieldCol = new HashMap<>();
		PropertyDescriptor[] fields = BeanUtils.getPropertyDescriptors(clasz);
		StringBuilder fieldsS = new StringBuilder();
		for(PropertyDescriptor targetPd : fields){
			String fieldName = targetPd.getName();
			if("class".equals(fieldName)) continue;
			if("serialVersionUID".equals(fieldName)) continue;
			String colName = DaoUtils.toTable(fieldName);
			fieldCol.put(fieldName, new MetadataEntry(fieldName, colName));
			fieldsS.append(",").append(colName);
		}
		fieldSelect = fieldsS.substring(1);
	}

	public String getFieldSelect() {
		if(fieldSelect == null){
			synchronized (this) {
				if(fieldSelect == null){
					buildFieldCol();
				}
			}
		}
		return fieldSelect;
	}

	
}
