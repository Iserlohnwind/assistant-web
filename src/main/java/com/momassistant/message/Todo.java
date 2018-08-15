package com.momassistant.message;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/15.
 */
@Data
public class Todo implements Runnable{
    private int id;
    private String openId;
    private String title;
    private String content;
    private String url;

    public Todo(String openId, String title, String content, String url) {
        this.openId = openId;
        this.title = title;
        this.content = content;
        this.url = url;
    }

    @Override
    public void run() {
        //1.发送 2.删除数据库todolog
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
