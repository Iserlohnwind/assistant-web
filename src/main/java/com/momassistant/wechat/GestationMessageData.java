package com.momassistant.wechat;

import java.io.Serializable;

public class GestationMessageData implements Serializable{
	private static final long serialVersionUID = -322361724914585497L;
	private WeiXinSendValue first;
	private WeiXinSendValue keyword1;
	private WeiXinSendValue keyword2;
	private WeiXinSendValue remark;


	public WeiXinSendValue getFirst() {
		return first;
	}

	public void setFirst(WeiXinSendValue first) {
		this.first = first;
	}

	public WeiXinSendValue getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(WeiXinSendValue keyword1) {
		this.keyword1 = keyword1;
	}

	public WeiXinSendValue getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(WeiXinSendValue keyword2) {
		this.keyword2 = keyword2;
	}

	public WeiXinSendValue getRemark() {
		return remark;
	}
	public void setRemark(WeiXinSendValue remark) {
		this.remark = remark;
	}
}

