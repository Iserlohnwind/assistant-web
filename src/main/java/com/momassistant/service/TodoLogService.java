package com.momassistant.service;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.enums.TodoMainType;
import com.momassistant.mapper.TodoLogMapper;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.message.GestationTodo;
import com.momassistant.message.LactationTodo;
import com.momassistant.message.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/20.
 */
@Service
public class TodoLogService {
    @Autowired
    private TodoLogMapper todoLogMapper;
    public void refreshTodoLog(Date sendDate, Todo todo) {
        //新提醒入库
        TodoLog todoLog = new TodoLog();
        todoLog.setUserId(todo.getUserId());
        todoLog.setOpenId(todo.getOpenId());
        todoLog.setDataJson(JSONObject.toJSONString(todo.getData()));
        todoLog.setUrl("");
        todoLog.setTypeId(todo.getTypeId());
        todoLog.setSendTime(sendDate);
        TodoMainType todoMainType = null;
        if (todo instanceof GestationTodo) {
            todoLog.setMainTypeId(TodoMainType.GESTATION.getType());
        } else {
            todoLog.setMainTypeId(TodoMainType.Lactation.getType());
            todoLog.setBabyId(((LactationTodo)todo).getBabyId());
        }
        if (todoLogMapper.countLog(todoLog) > 0) {
            todoLogMapper.updateLog(todoLog);
        } else {
            todoLogMapper.insertLog(todoLog);
        }
    }
}
