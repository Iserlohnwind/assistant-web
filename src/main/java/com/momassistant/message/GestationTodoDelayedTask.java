package com.momassistant.message;

import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.mapper.TodoLogMapper;
import com.momassistant.mapper.TodoTypeDetailMapper;
import com.momassistant.mapper.TodoTypeMapper;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoType;
import com.momassistant.utils.DateUtil;
import com.momassistant.utils.WechatAuthUtil;
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
    private static final String FIRST_TEMPLATE = "亲爱的准妈妈,%s天后您将进行%s";

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

                if (checkTodoNotifySwitchOn(todo.getUserId())){
                    //发送过程，暂未实现
                    //....
                    WechatAuthUtil.sendMsg(TodoMainType.GESTATION, todo.getOpenId(), todo.getData());
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
        Map<String, String> data = new HashMap<>();
        Date sendTime = calSendTime(oldTodo.getUserId(), todoType);
        int timeRemaining = DateUtil.getIntervalOfCalendarDay(sendTime, new Date());
        data.put("first", String.format(FIRST_TEMPLATE, timeRemaining, todoType.getTitle()));
        data.put("keyword1", DateUtil.format(sendTime));
        data.put("keyword2", "xxxx");
        data.put("remark", "点击查看本次产检更多注意事项吧~");
        GestationTodo newTodo = new GestationTodo(todoType.getId(), oldTodo.getUserId(), oldTodo.getOpenId(), data);
        put(sendTime, newTodo);
        TodoLog todoLog = new TodoLog();
        createTodoLog(sendTime, newTodo);
        todoLogMapper.updateLog(todoLog);
    }

    private TodoLog createTodoLog(Date sendTime, Todo todo) {
        //新提醒入库
        TodoLog todoLog = new TodoLog();
        todoLog.setUserId(todo.getUserId());
        todoLog.setOpenId(todo.getOpenId());
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
