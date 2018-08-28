package com.momassistant.message;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoTypeDetail;
import com.momassistant.wechat.WeiXinTemplate;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zhufeng on 2018/8/16.
 */
@Data
public class GestationTodo extends Todo {

    public GestationTodo(TodoLog todoLog) {
        super(todoLog);
    }

    public GestationTodo(int userId) {
        super(userId);
    }

    public GestationTodo(int typeId, int userId, String openId, WeiXinTemplate weiXinTemplate) {
        super(typeId, userId, openId, weiXinTemplate);
    }


    @Override
    public String toString() {
        return String.valueOf(this.getUserId());
    }
}
