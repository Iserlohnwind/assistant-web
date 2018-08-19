package com.momassistant.message;

import com.momassistant.mapper.model.BabyInfo;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoTypeDetail;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by zhufeng on 2018/8/16.
 */
@Data
public class LactationTodo extends Todo {
    private int babyId;
    private String babyName;

    public LactationTodo(int typeId, int userId, String openId, Map<String, String> data, BabyInfo babyInfo) {
        super(typeId, userId, openId, data);
        this.babyId = babyInfo.getBabyId();
        this.babyName = babyInfo.getBabyName();
    }


    public LactationTodo(int babyId) {
        super();
        this.babyId = babyId;
    }

    public LactationTodo(TodoLog todoLog) {
        super(todoLog);
        this.babyId = todoLog.getBabyId();
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LactationTodo todo = (LactationTodo) o;
        return this.getBabyId() == todo.getBabyId();

    }

    @Override
    public int hashCode() {
        return this.getBabyId();
    }
}
