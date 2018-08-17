package com.momassistant.message;

import com.momassistant.mapper.model.TodoTypeDetail;

import java.util.List;

/**
 * Created by zhufeng on 2018/8/16.
 */
public class LactationTodo extends Todo {
    private int babyId;

    public LactationTodo(int typeId, int userId, String openId, String title, List<TodoTypeDetail> todoTypeDetailList, int babyId) {
        super(typeId, userId, openId, title, todoTypeDetailList);
        this.babyId = babyId;
    }
}
