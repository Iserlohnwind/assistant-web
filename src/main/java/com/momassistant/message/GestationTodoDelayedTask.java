package com.momassistant.message;

import com.momassistant.enums.TodoMainType;
import com.momassistant.mapper.TodoLogMapper;
import com.momassistant.mapper.TodoTypeMapper;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoType;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/16.
 * 孕期todo task
 */
@Component
public class GestationTodoDelayedTask extends DelayedTask<GestationTodo> {
    @Autowired
    private TodoTypeMapper todoTypeMapper;
    @Autowired
    private TodoLogMapper todoLogMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    public void initQueue() {
        System.out.println();
        List<TodoLog> todoLogList = null;
        int minId = 0;
        while (!CollectionUtils.isEmpty(todoLogList = todoLogMapper.paginateLogs(minId, TodoMainType.GESTATION.getType()))) {
            for (TodoLog todoLog : todoLogList) {
                UserInfo userInfo = userInfoMapper.getUserDetail(todoLog.getUserId());
                GestationTodo todo = new GestationTodo(todoLog.getTypeId(), todoLog.getUserId(), userInfo.getOpenId(), todoLog.getTitle(), todoLog.getContent(), todoLog.getUrl());
                put(todoLog.getSendTime(), todo);
                minId = todoLog.getId();
            }
        }
    }

    @Override
    public Runnable excuteRunable(GestationTodo todo) {
        return new Runnable() {
            @Override
            public void run() {
                //发送过程，暂未实现
                //....
                TodoType todoType = todoTypeMapper.findByPreId(todo.getTypeId());
                //新建下一个提醒,存入队列
                GestationTodo newTodo = createNextTodo(todo, todoType);
                Date sendTime = calSendTime(todo.getUserId(), todoType);
                put(sendTime, newTodo);
                TodoLog todoLog = new TodoLog();
                createTodoLog(sendTime, newTodo);
                todoLogMapper.updateLog(todoLog);
            }
        };
    }

    private GestationTodo createNextTodo(GestationTodo oldTodo, TodoType nextTodoType) {
        GestationTodo newTodo = new GestationTodo(nextTodoType.getId(), oldTodo.getUserId(), oldTodo.getOpenId(), nextTodoType.getTitleTemplate(), nextTodoType.getContentTemplate(), nextTodoType.getUrlTemplate());
        return newTodo;
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

    private Date calSendTime(int userId, TodoType todoType) {
        Date edc = userInfoMapper.getUserDetail(userId).getEdc();
        Date sendTime = DateUtil.addDays(edc, todoType.getTodoDay());
        return sendTime;
    }
}
