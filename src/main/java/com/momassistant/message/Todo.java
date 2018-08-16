package com.momassistant.message;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/15.
 */
@Data
public class Todo{
    private int typeId;
    private int userId;
    private String openId;
    private String title;
    private String content;
    private String url;

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

}
