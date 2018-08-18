package com.momassistant.message;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoTypeDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/15.
 */
public class Todo{
    private int typeId;
    private int userId;
    private String openId;
    private String title;
    private String content;

    public Todo() {
    }

    public Todo(TodoLog todoLog) {
        this.typeId = todoLog.getTypeId();
        this.userId = todoLog.getUserId();
        this.title = todoLog.getTitle();
        this.content = todoLog.getContent();
        this.openId = todoLog.getOpendId();
    }

    public Todo(int typeId, int userId, String openId, String title, List<TodoTypeDetail> todoTypeDetailList) {
        this.typeId = typeId;
        this.userId = userId;
        this.openId = openId;
        this.title = title;
        StringBuilder contentSb = new StringBuilder();
        for (TodoTypeDetail todoTypeDetail : todoTypeDetailList) {
            contentSb.append(todoTypeDetail.getTitle()).append("\n").append(todoTypeDetail.getContent()).append("\n");
        }

    }
    @Override
    public String toString() {
        return "Todo{" +
                "openId='" + openId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
