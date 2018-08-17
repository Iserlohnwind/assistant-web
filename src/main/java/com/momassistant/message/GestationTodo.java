package com.momassistant.message;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoTypeDetail;

import java.util.List;

/**
 * Created by zhufeng on 2018/8/16.
 */
public class GestationTodo extends Todo{

    public GestationTodo(TodoLog todoLog, String openId) {
        super(todoLog, openId);
    }

    public GestationTodo(int userId) {
        super(userId);
    }

    public GestationTodo(int typeId, int userId, String openId, String title, List<TodoTypeDetail> todoTypeDetailList) {
        super(typeId, userId, openId, title, todoTypeDetailList);
    }
}
