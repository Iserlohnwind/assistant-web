package com.momassistant.message;

import com.momassistant.enums.TodoMainType;
import com.momassistant.mapper.TodoLogMapper;
import com.momassistant.mapper.TodoTypeMapper;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoType;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.service.CommonTodoService;
import com.momassistant.service.GestationTodoService;
import com.momassistant.utils.SpringContextAware;
import com.momassistant.wechat.WeiXinMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhufeng on 2018/8/16.
 * 孕期todo task
 */
@Component("gestationTodoDelayedTask")
public class GestationTodoDelayedTask extends DelayedTask<GestationTodo> {
    @Autowired
    private TodoTypeMapper todoTypeMapper;
    @Autowired
    private TodoLogMapper todoLogMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private CommonTodoService commonTodoService;
    @Autowired
    private WeiXinMessageService weiXinMessageService;

    public void initQueue() {
        List<TodoLog> todoLogList = null;
        int minId = 0;
        while (!CollectionUtils.isEmpty(todoLogList = todoLogMapper.paginateLogs(minId, TodoMainType.GESTATION.getType()))) {
            for (TodoLog todoLog : todoLogList) {
                GestationTodo todo = new GestationTodo(todoLog);
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

                if (commonTodoService.checkTodoNotifySwitchOn(todo.getUserId())){
                    //发送过程，暂未实现
                    weiXinMessageService.sendTemplateMessage(todo.getWeiXinTemplate());
                }
                //新建下一个提醒,存入队列
                createNextTodo(todo);
            }
        };
    }

    private void createNextTodo(GestationTodo oldTodo) {
        GestationTodoService gestationTodoService = SpringContextAware.getBean(GestationTodoService.class);
        TodoType todoType = todoTypeMapper.findByPreId(oldTodo.getTypeId());
        UserInfo userInfo = userInfoMapper.getUserDetail(oldTodo.getUserId());
        if (todoType != null) {
            gestationTodoService.createTodo(userInfo, todoType);
        }
    }
}
