package com.momassistant.message;

import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.mapper.TodoLogMapper;
import com.momassistant.mapper.TodoTypeDetailMapper;
import com.momassistant.mapper.TodoTypeMapper;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoType;
import com.momassistant.mapper.model.TodoTypeDetail;
import com.momassistant.mapper.model.UserInfo;
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
@Component("gestationTodoDelayedTask")
public class GestationTodoDelayedTask extends DelayedTask<GestationTodo> {
    @Autowired
    private TodoTypeMapper todoTypeMapper;
    @Autowired
    private TodoTypeDetailMapper todoTypeDetailMapper;
    @Autowired
    private TodoLogMapper todoLogMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    public void initQueue() {
        List<TodoLog> todoLogList = null;
        int minId = 0;
        while (!CollectionUtils.isEmpty(todoLogList = todoLogMapper.paginateLogs(minId, TodoMainType.GESTATION.getType()))) {
            for (TodoLog todoLog : todoLogList) {
                UserInfo userInfo = userInfoMapper.getUserDetail(todoLog.getUserId());
                GestationTodo todo = new GestationTodo(todoLog, userInfo.getOpenId());
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


    private void createNextTodo(GestationTodo oldTodo) {
        TodoType todoType = todoTypeMapper.findByPreId(oldTodo.getTypeId());
        List<TodoTypeDetail> todoTypeDetailList = todoTypeDetailMapper.findByTypeId(todoType.getId());
        GestationTodo newTodo = new GestationTodo(todoType.getId(), oldTodo.getUserId(), oldTodo.getOpenId(), todoType.getTitle(), todoTypeDetailList);
        Date sendTime = calSendTime(oldTodo.getUserId(), todoType);
        put(sendTime, newTodo);
        TodoLog todoLog = new TodoLog();
        createTodoLog(sendTime, newTodo);
        todoLogMapper.updateLog(todoLog);
    }

    private TodoLog createTodoLog(Date sendTime, Todo todo) {
        //新提醒入库
        TodoLog todoLog = new TodoLog();
        todoLog.setUserId(todo.getUserId());
        todoLog.setOpendId(todo.getOpenId());
        todoLog.setTitle(todo.getTitle());
        todoLog.setContent(todo.getContent());
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
