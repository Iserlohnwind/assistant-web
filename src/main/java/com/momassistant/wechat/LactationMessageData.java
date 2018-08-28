package com.momassistant.wechat;

import java.io.Serializable;

public class LactationMessageData implements Serializable{
	private static final long serialVersionUID = -8181800141515725346L;
	private WeiXinSendValue first;
	private WeiXinSendValue keyword1;
	private WeiXinSendValue keyword2;
	private WeiXinSendValue keyword3;
	private WeiXinSendValue keyword4;

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

	public WeiXinSendValue getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(WeiXinSendValue keyword3) {
		this.keyword3 = keyword3;
	}

	public WeiXinSendValue getKeyword4() {
		return keyword4;
	}

	public void setKeyword4(WeiXinSendValue keyword4) {
		this.keyword4 = keyword4;
	}
}

