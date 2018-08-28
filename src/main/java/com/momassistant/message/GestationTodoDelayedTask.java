package com.momassistant.message;

import com.momassistant.mapper.TodoTypeMapper;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.model.TodoType;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.service.GestationTodoService;
import com.momassistant.utils.SpringContextAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhufeng on 2018/8/16.
 * 孕期todo task
 */
@Component("gestationTodoDelayedTask")
public class GestationTodoDelayedTask extends DelayedTask<GestationTodo> {
    @Autowired
    private TodoTypeMapper todoTypeMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    protected void createNextTodo(Todo oldTodo) {
        GestationTodoService gestationTodoService = SpringContextAware.getBean(GestationTodoService.class);
        TodoType todoType = todoTypeMapper.findByPreId(oldTodo.getTypeId());
        UserInfo userInfo = userInfoMapper.getUserDetail(oldTodo.getUserId());
        if (todoType != null) {
            gestationTodoService.createTodo(userInfo, todoType);
        }
    }

    @Override
    protected void initDelayedMessageSerializer() {
        delayedMessageSerializer = new DelayedMessageSerializer("/data/queue/gestation/");
    }
}
