package com.momassistant.message;

import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.mapper.*;
import com.momassistant.mapper.model.*;
import com.momassistant.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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
    private TodoTypeDetailMapper todoTypeDetailMapper;
    @Autowired
    private TodoLogMapper todoLogMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BabyInfoMapper babyInfoMapper;

    public void initQueue() {
        List<TodoLog> todoLogList = null;
        int minId = 0;
        while (!CollectionUtils.isEmpty(todoLogList = todoLogMapper.paginateLogs(minId, TodoMainType.Lactation.getType()))) {
            for (TodoLog todoLog : todoLogList) {
                UserInfo userInfo = userInfoMapper.getUserDetail(todoLog.getUserId());
                LactationTodo todo = new LactationTodo(todoLog);
                put(todoLog.getSendTime(), todo);
                minId = todoLog.getId();
            }
        }
    }

    @Override
    public Runnable excuteRunable(LactationTodo todo) {
        return new Runnable() {
            @Override
            public void run() {

                if (checkTodoNotifySwitchOn(todo.getUserId())){
                    //发送过程，暂未实现
                    //....
                }
                //新建下一个提醒,存入队列
                createNextTodo(todo);
            }
        };
    }

    private boolean checkTodoNotifySwitchOn(int userId) {
        return userInfoMapper.getTodoNotifySwitch(userId) == TodoNotifySwitch.ON.getVal();
    }


    private void createNextTodo(LactationTodo oldTodo) {
        TodoType todoType = todoTypeMapper.findByPreId(oldTodo.getTypeId());
        List<TodoTypeDetail> todoTypeDetailList = todoTypeDetailMapper.findByTypeId(todoType.getId());
        BabyInfo babyInfo = babyInfoMapper.findByUserIdAndBabyId(oldTodo.getUserId(), oldTodo.getBabyId());
        LactationTodo newTodo = new LactationTodo(todoType.getId(), oldTodo.getUserId(), oldTodo.getOpenId(), todoType.getTitle(), todoTypeDetailList, babyInfo);
        Date sendTime = calSendTime(babyInfo, todoType);
        put(sendTime, newTodo);
        TodoLog todoLog = new TodoLog();
        createTodoLog(sendTime, newTodo, babyInfo);
        todoLogMapper.updateLog(todoLog);
    }

    private TodoLog createTodoLog(Date sendTime, Todo todo, BabyInfo babyInfo) {
        //新提醒入库
        TodoLog todoLog = new TodoLog();
        todoLog.setUserId(todo.getUserId());
        todoLog.setOpendId(todo.getOpenId());
        todoLog.setBabyId(babyInfo.getBabyId());
        todoLog.setBabyName(babyInfo.getBabyName());
        todoLog.setTitle(todo.getTitle());
        todoLog.setContent(todo.getContent());
        todoLog.setTypeId(todo.getTypeId());
        todoLog.setSendTime(sendTime);
        todoLog.setMainTypeId(TodoMainType.Lactation.getType());
        return todoLog;
    }

    private Date calSendTime(BabyInfo babyInfo, TodoType todoType) {
        Date birthday = babyInfo.getBabyBirthday();
        Date sendTime = DateUtil.addDays(birthday, todoType.getTodoDay());
        return sendTime;
    }
}
