package com.momassistant.wechat;

import java.io.Serializable;

public class WeiXinTemplate<DATA> implements Serializable{

	private static final long serialVersionUID = 3320725223156002791L;
	private String touser;
	private String template_id;
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
