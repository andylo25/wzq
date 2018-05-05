package com.andy.gomoku.entity;

public class ConfCommon extends BaseEntity{

	private static final long serialVersionUID = 1L;

	public ConfCommon() {
	}
	
	public ConfCommon(Long id) {
		super(id);
	}
	
	
	private String nid;
	
	private String value;
	
	private String remark;

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static String table() {
		return "conf_common";
	}
	

}
