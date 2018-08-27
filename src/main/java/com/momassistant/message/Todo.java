package com.momassistant.message;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.wechat.WeiXinTemplate;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by zhufeng on 2018/8/15.
 */
public class Todo implements Serializable{
    private int typeId;
    private int userId;
    private String openId;
    private WeiXinTemplate weiXinTemplate;


    public Todo() {
    }

    public Todo(TodoLog todoLog) {
        this.typeId = todoLog.getTypeId();
        this.userId = todoLog.getUserId();
        this.openId = todoLog.getOpenId();
    }

    public Todo(int typeId, int userId, String openId, WeiXinTemplate weiXinTemplate) {
        this.typeId = typeId;
        this.userId = userId;
        this.openId = openId;
        this.weiXinTemplate = weiXinTemplate;

    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public WeiXinTemplate getWeiXinTemplate() {
        return weiXinTemplate;
    }

    public void setWeiXinTemplate(WeiXinTemplate weiXinTemplate) {
        this.weiXinTemplate = weiXinTemplate;
    }


    @Override
    public boolean equals(Object o) {
        return o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
