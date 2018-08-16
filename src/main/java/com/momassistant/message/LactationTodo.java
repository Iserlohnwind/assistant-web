package com.momassistant.message;

/**
 * Created by zhufeng on 2018/8/16.
 */
public class LactationTodo extends Todo {
    private int babyId;

    public LactationTodo(int typeId, int userId, String openId, String title, String content, String url, int babyId) {
        super(typeId, userId, openId, title, content, url);
        this.babyId = babyId;
    }
}
