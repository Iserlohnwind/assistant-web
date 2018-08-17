package com.momassistant.message;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/15.
 */
public class Todo{
    private int typeId;
    private int userId;
    private String openId;
    private String title;
    private String content;
    private String url;

    public Todo(int userId) {
        this.userId = userId;
    }

    public Todo(int typeId, int userId, String openId, String title, String content, String url) {
        this.typeId = typeId;
        this.userId = userId;
        this.openId = openId;
        this.title = title;
        this.content = content;
        this.url = url;
    }
    @Override
    public String toString() {
        return "Todo{" +
                "openId='" + openId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Todo todo = (Todo) o;

        return userId == todo.userId;

    }

    @Override
    public int hashCode() {
        return userId;
    }
}
