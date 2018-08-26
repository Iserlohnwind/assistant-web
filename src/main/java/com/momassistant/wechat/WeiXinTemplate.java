package com.momassistant.wechat;

import java.io.Serializable;

public class WeiXinTemplate<DATA> implements Serializable{

	private static final long serialVersionUID = -1L;
	private String touser;
	private String template_id;
	private String url;
	private Miniprogram miniprogram;
	private DATA data;
	
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String templateId) {
		template_id = templateId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public DATA getData() {
		return data;
	}
	public void setData(DATA data) {
		this.data = data;
	}

	public Miniprogram getMiniprogram() {
		return miniprogram;
	}

	public void setMiniprogram(Miniprogram miniprogram) {
		this.miniprogram = miniprogram;
	}
}
