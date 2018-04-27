package com.andy.gomoku.base;

import java.io.Serializable;

public class RespVO implements Serializable {

	private static final long serialVersionUID = 4022828639456713673L;

	public static final int OK = 200;
	public static final int ERROR = 100;
	public static final int ERROR_NET = 102; // 网络异常
	public static final int LOGINERR = 250;

	private int status;
	private Long id;
	private Object data;
	private String description;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isOK(){
		return OK == status;
	}
	
	public boolean isNetError(){
		return ERROR_NET == status;
	}
	
	public static RespVO createResp(int status){
		RespVO dy = new RespVO();
		dy.setStatus(status);
		return dy;
	}
	
	public static RespVO createResp(int status,String description){
		RespVO dy = new RespVO();
		dy.setStatus(status);
		dy.setDescription(description);
		return dy;
	}
	
	public static RespVO createSuccResp(){
		RespVO dy = new RespVO();
		dy.setStatus(OK);
		return dy;
	}
	
	public static RespVO createErrorResp(){
		RespVO dy = new RespVO();
		dy.setStatus(ERROR);
		return dy;
	}
	
	public static RespVO createSuccessJsonResonse(Object data) {
		return createSuccessJsonResonse(data, "OK");
	}
	
	public static RespVO createSuccessJsonResonse(Object data, String description) {
		RespVO response = new RespVO();
		response.setStatus(RespVO.OK);
		response.setDescription(description);
		response.setData(data);
		
		return response;
	}
	
	public static RespVO createSuccessJsonResonse(String description) {
		RespVO response = new RespVO();
		response.setStatus(RespVO.OK);
		response.setDescription(description);
		
		return response;
	}
	
	public static RespVO createErrorJsonResonse(String errorMsg) {
		RespVO response = new RespVO();
		response.setStatus(RespVO.ERROR);
		response.setDescription(errorMsg);
		
		return response;
	}
	
	public static RespVO createLoginJsonResonse(String errorMsg) {
		RespVO response = new RespVO();
		response.setStatus(RespVO.LOGINERR);
		response.setDescription(errorMsg);
		
		return response;
	}

}