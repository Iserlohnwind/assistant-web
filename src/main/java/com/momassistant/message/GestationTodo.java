package com.momassistant.message;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoTypeDetail;
import lombok.Data;

import java.util.List;

/**
 * Created by zhufeng on 2018/8/16.
 */
@Data
public class GestationTodo extends Todo{

    public GestationTodo(TodoLog todoLog) {
        super(todoLog);
    }

    public GestationTodo(int userId) {
        super();
        this.setUserId(userId);
    }

    public GestationTodo(int typeId, int userId, String openId, String title, List<TodoTypeDetail> todoTypeDetailList) {
        super(typeId, userId, openId, title, todoTypeDetailList);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Todo todo = (Todo) o;

        return this.getUserId() == todo.getUserId();

    }

    @Override
    public int hashCode() {
        return this.getUserId();
    }

}
