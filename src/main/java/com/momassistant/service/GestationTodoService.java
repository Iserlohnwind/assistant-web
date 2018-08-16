package com.momassistant.service;

import com.momassistant.enums.TodoMainType;
import com.momassistant.mapper.TodoLogMapper;
import com.momassistant.mapper.TodoTypeMapper;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoType;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.message.DelayedTask;
import com.momassistant.message.GestationTodo;
import com.momassistant.message.Todo;
import com.momassistant.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/16.
 */
@Service
public class GestationTodoService {
    private static final int DAY_INTERVAL = 280;
    @Autowired
    private TodoTypeMapper todoTypeMapper;
    @Autowired
    private TodoLogMapper todoLogMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    @Qualifier("gestationTodoDelayedTask")
    private DelayedTask gestationTodoDelayedTask;

    public void initGestationTodo(int userId) {
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        Date doc = DateUtil.addDays(userInfo.getEdc(), -DAY_INTERVAL);
        int minTodoDay = (int) ((new Date().getTime() - doc.getTime()) / (1000*3600*24));
        TodoType todoType = todoTypeMapper.findByMainTypeIdAndMinTodoDay(1, minTodoDay);
        if (todoType != null) {
            GestationTodo todo = new GestationTodo(todoType.getId(), userInfo.getUserId(), userInfo.getOpenId(), todoType.getTitleTemplate(), todoType.getContentTemplate(), todoType.getUrlTemplate());
            Date sendTime = calSendTime(userInfo.getEdc(), todoType);
            TodoLog todoLog = createTodoLog(sendTime, todo);
            todoLogMapper.insertLog(todoLog);
            gestationTodoDelayedTask.put(sendTime, todo);
        }
    }


    private Date calSendTime(Date edc, TodoType todoType) {
        Date sendTime = DateUtil.addDays(edc, todoType.getTodoDay());
        return sendTime;
    }


    private TodoLog createTodoLog(Date sendTime, Todo todo) {
        //新提醒入库
        TodoLog todoLog = new TodoLog();
        todoLog.setUserId(todo.getUserId());
        todoLog.setOpendId(todo.getOpenId());
        todoLog.setTitle(todo.getTitle());
        todoLog.setContent(todo.getContent());
        todoLog.setUrl(todo.getUrl());
        todoLog.setTypeId(todo.getTypeId());
        todoLog.setSendTime(sendTime);
        todoLog.setMainTypeId(TodoMainType.GESTATION.getType());
        return todoLog;
    }
}
