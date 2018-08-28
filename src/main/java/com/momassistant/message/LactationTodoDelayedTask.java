package com.momassistant.message;

import com.momassistant.mapper.*;
import com.momassistant.mapper.model.*;
import com.momassistant.service.LactationTodoService;
import com.momassistant.utils.SpringContextAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by zhufeng on 2018/8/16.
 * 孕期todo task
 */
@Component("lactationTodoDelayedTask")
public class LactationTodoDelayedTask extends DelayedTask<LactationTodo> {
    @Autowired
    private TodoTypeMapper todoTypeMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BabyInfoMapper babyInfoMapper;


    protected void createNextTodo(Todo oldTodo) {
        LactationTodo lactationTodo = (LactationTodo)oldTodo;
        LactationTodoService lactationTodoService = SpringContextAware.getBean(LactationTodoService.class);
        TodoType todoType = todoTypeMapper.findByPreId(oldTodo.getTypeId());
        UserInfo userInfo = userInfoMapper.getUserDetail(oldTodo.getUserId());
        BabyInfo babyInfo = babyInfoMapper.findByUserIdAndBabyId(oldTodo.getUserId(), lactationTodo.getBabyId());
        lactationTodoService.createTodo(todoType, userInfo, babyInfo);
    }


    @Override
    protected void initDelayedMessageSerializer() {
        delayedMessageSerializer = new DelayedMessageSerializer("/data/queue/lactation/");
    }

}
