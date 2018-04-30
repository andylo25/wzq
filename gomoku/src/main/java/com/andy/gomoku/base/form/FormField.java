package com.andy.gomoku.base.form;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FormField implements Serializable {
	private static final long serialVersionUID = -8269558838532027061L;

	private String name;
	
	private String text;
	
	private String type = "input";
	
	private String notice;
	
	private List<?> options;
	
	private String direc; //指令
	
	private String verify;
	
	private String hint;
	
	private String required;
	
	private Object value;
	
	private String field;
	
	private Boolean hide;
	
	private String remarks;
	
	private Boolean tab;

	private Boolean haschild;
	
	private FormField formField;
	
	private String unit;
	
	private Map verifyoption;
	
	private String mark;
	
	private String parentname;
	
	private Map uploadify;
	
	private String message_id;
	
	private String placeholder;
	
	private String pre_text;
	
	private String br;
	
	private String clasz;
	
	private String url;
	private List linklist;
	private String dataparams;
	
	public static class Builder{
		private FormField formField = new FormField();
		
		public Builder name(String name){
			formField.setName(name);
			return this;
		}
		public Builder text(String text){
			formField.setText(text);
			return this;
		}
		public Builder type(String type){
			formField.setType(type);
			return this;
		}
		public Builder options(List<?> options){
			formField.setOptions(options);
			return this;
		}
		public Builder options(String codeType){
//			formField.setOptions(DictUtils.getOptions(codeType));
			return this;
		}

		public Builder optionsInt(String codeType) {
//			formField.setOptions(DictUtils.getOptionsInt(codeType));
			return this;
		}
		public Builder verify(String verify){
			formField.setVerify(verify);
			return this;
		}
		public Builder placeholder(String placeholder){
			formField.setPlaceholder(placeholder);
			return this;
		}
		public Builder remarks(String remarks){
			formField.setRemarks(remarks);
			return this;
		}
		public Builder value(Object value) {
			formField.setValue(value);
			return this;
		}
		public Builder preText(String preText) {
			formField.setPreText(preText);
			return this;
		}
		public Builder br(String br) {
			formField.setBr(br);
			return this;
		}
		public Builder clasz(String clasz) {
			formField.setClasz(clasz);
			return this;
		}
		/**
		 * 前端请求后台数据(比如多级联动)
		 * @param url
		 * @return
		 * @author likf
		 */
		public Builder url(String url) {
		    formField.setUrl(url);
		    return this;
		}
		/**
		 * 用于多级联动
		 * @param linklist
		 * @return
		 * @author likf
		 */
		public Builder linklist(List linklist) {
		    formField.setLinklist(linklist);
		    return this;
		}
		/**
		 * 
		 * 用于多级联动及其他参数传递
		 * @param dataparams
		 * @return
		 * @author likf
		 */
		public Builder dataparams(String dataparams) {
		    formField.setDataparams(dataparams);
		    return this;
		}
		
		public Builder direc(String direc) {
			formField.setDirec(direc);
			return this;
		}
		
		public FormField build(){
			return formField;
		}
	}
	
	public static Builder builder(){
		return new Builder();
	}
	
	public FormField getFormField() {
		return formField;
	}

	public void setFormField(FormField formField) {
		this.formField = formField;
	}

	public Boolean isTab() {
		return tab;
	}
	
	public void setTab(Boolean tab) {
		this.tab = tab;
	}
	

	public Boolean isHaschild() {
		return haschild;
	}

	public void setHaschild(Boolean haschild) {
		this.haschild = haschild;
	}

	public Boolean isHide() {
		return hide;
	}

	public void setHide(Boolean hide) {
		this.hide = hide;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}



	public List<?> getOptions() {
		return options;
	}

	public void setOptions(List<?> options) {
		this.options = options;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Map getVerifyoption() {
		return verifyoption;
	}

	public void setVerifyoption(Map verifyoption) {
		this.verifyoption = verifyoption;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	public Map getUploadify() {
		return uploadify;
	}

	public void setUploadify(Map uploadify) {
		this.uploadify = uploadify;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPreText() {
		return pre_text;
	}

	public void setPreText(String preText) {
		this.pre_text = preText;
	}

    
    public String getUrl() {
        return url;
    }

    
    public void setUrl(String url) {
        this.url = url;
    }

    
    public List getLinklist() {
        return linklist;
    }

    
    public void setLinklist(List linklist) {
        this.linklist = linklist;
    }

    
    public String getDataparams() {
        return dataparams;
    }

    
    public void setDataparams(String dataparams) {
        this.dataparams = dataparams;
    }

	public String getBr() {
		return br;
	}

	public void setBr(String br) {
		this.br = br;
	}

	public String getClasz() {
		return clasz;
	}

	public void setClasz(String clasz) {
		this.clasz = clasz;
	}

	public String getDirec() {
		return direc;
	}

	public void setDirec(String direc) {
		this.direc = direc;
	}
	
}