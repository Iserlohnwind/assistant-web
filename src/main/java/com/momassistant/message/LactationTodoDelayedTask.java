package com.momassistant.message;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.mapper.*;
import com.momassistant.mapper.model.*;
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
                    WechatAuthUtil.sendMsg(TodoMainType.Lactation, todo.getOpenId(), todo.getData());
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

        Map<String, String> data = new HashMap<>();
        Date sendTime = calSendTime(babyInfo, todoType);
        data.put("first", "尊敬的家长,您好!您的孩子今日需要接种疫苗,请及时安排您的孩子到指定接种点进行接种!");
        data.put("keyword1", String.format("姓名：%s", babyInfo.getBabyName()));
        data.put("keyword2", String.format("性别：%s", babyInfo.getBabyGender()));
        data.put("keyword3", DateUtil.format(sendTime));
        data.put("keyword4", String.format("计划接种疫苗：%s", "xx"));
        data.put("remark", "注意事项：%s");

        LactationTodo newTodo = new LactationTodo(todoType.getId(), oldTodo.getUserId(), oldTodo.getOpenId(), data, babyInfo);
        put(sendTime, newTodo);
        TodoLog todoLog = new TodoLog();
        createTodoLog(sendTime, newTodo, babyInfo);
        todoLogMapper.updateLog(todoLog);
    }

    private TodoLog createTodoLog(Date sendTime, Todo todo, BabyInfo babyInfo) {
        //新提醒入库
        TodoLog todoLog = new TodoLog();
        todoLog.setUserId(todo.getUserId());
        todoLog.setOpenId(todo.getOpenId());
        todoLog.setBabyId(babyInfo.getBabyId());
        todoLog.setDataJson(JSONObject.toJSONString(todo.getData()));
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
