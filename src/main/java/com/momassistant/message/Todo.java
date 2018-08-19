package com.momassistant.message;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoTypeDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhufeng on 2018/8/15.
 */
public class Todo{
    private int typeId;
    private int userId;
    private String openId;
    private Map<String, String> data;


    public Todo() {
    }

    public Todo(TodoLog todoLog) {
        this.typeId = todoLog.getTypeId();
        this.userId = todoLog.getUserId();
        this.openId = todoLog.getOpendId();
    }

    public Todo(int typeId, int userId, String openId, Map<String, String> data) {
        this.typeId = typeId;
        this.userId = userId;
        this.openId = openId;
        this.data = data;

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

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
