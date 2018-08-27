package com.momassistant.message;

import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.mapper.*;
import com.momassistant.mapper.model.*;
import com.momassistant.service.LactationTodoService;
import com.momassistant.utils.DelayedMessageSerializer;
import com.momassistant.utils.SpringContextAware;
import com.momassistant.wechat.WeiXinMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by zhufeng on 2018/8/16.
 * 孕期todo task
 */
@Component("lactationTodoDelayedTask")
public class LactationTodoDelayedTask extends DelayedTask<LactationTodo> {
    @Autowired
    private TodoTypeMapper todoTypeMapper;
    @Autowired
    private TodoLogMapper todoLogMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BabyInfoMapper babyInfoMapper;
    @Autowired
    private WeiXinMessageService weiXinMessageService;


//
//
//    public void initQueue() {
////        List<TodoLog> todoLogList = null;
////        int minId = 0;
////        while (!CollectionUtils.isEmpty(todoLogList = todoLogMapper.paginateLogs(minId, TodoMainType.Lactation.getType()))) {
////            for (TodoLog todoLog : todoLogList) {
////                LactationTodo todo = new LactationTodo(todoLog);
////                put(todoLog.getSendTime(), todo);
////                minId = todoLog.getId();
////            }
////        }
//
//
//    }

    @Override
    public Runnable excuteRunable(LactationTodo todo) {
        return new Runnable() {
            @Override
            public void run() {

                if (checkTodoNotifySwitchOn(todo.getUserId())){
                    //发送过程，暂未实现
                    //....
                    weiXinMessageService.sendTemplateMessage(todo.getWeiXinTemplate());
                }
                //新建下一个提醒,存入队列
                createNextTodo(todo);
            }
        };
    }

    private boolean checkTodoNotifySwitchOn(int userId) {
        Integer todoNotifySwitch = userInfoMapper.getTodoNotifySwitch(userId);
        int _todoNotifyWitch = todoNotifySwitch != null ? todoNotifySwitch : 0;
        return _todoNotifyWitch == TodoNotifySwitch.ON.getVal();
    }


    private void createNextTodo(LactationTodo oldTodo) {
        LactationTodoService lactationTodoService = SpringContextAware.getBean(LactationTodoService.class);
        TodoType todoType = todoTypeMapper.findByPreId(oldTodo.getTypeId());
        UserInfo userInfo = userInfoMapper.getUserDetail(oldTodo.getUserId());
        BabyInfo babyInfo = babyInfoMapper.findByUserIdAndBabyId(oldTodo.getUserId(), oldTodo.getBabyId());
        lactationTodoService.createTodo(todoType, userInfo, babyInfo);
    }


    @Override
    public void setDelayedMessageSerializer() {
        delayedMessageSerializer = new DelayedMessageSerializer("/data/queue/lactation/");
    }

}
