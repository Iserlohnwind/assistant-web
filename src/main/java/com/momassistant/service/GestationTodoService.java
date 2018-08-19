package com.momassistant.service;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.entity.response.TodoDetailItem;
import com.momassistant.entity.response.TodoItem;
import com.momassistant.entity.response.TodoDetailResp;
import com.momassistant.entity.response.UserGestationTodoResp;
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
import com.momassistant.message.DelayedMessage;
import com.momassistant.message.DelayedTask;
import com.momassistant.message.GestationTodo;
import com.momassistant.message.Todo;
import com.momassistant.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhufeng on 2018/8/16.
 */
@Service
public class GestationTodoService {
    private static final String FIRST_TEMPLATE = "亲爱的准妈妈,%s天后您将进行%s";
    private static final int DAY_INTERVAL = 280;
    @Autowired
    private TodoTypeMapper todoTypeMapper;
    @Autowired
    private TodoTypeDetailMapper todoTypeDetailMapper;
    @Autowired
    private TodoLogMapper todoLogMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    @Qualifier("gestationTodoDelayedTask")
    private DelayedTask gestationTodoDelayedTask;

    /**
     * 修改孕期的时候初始化todo
     * @param userId
     */
    @Async
    public void initGestationTodo(int userId) {
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        clearOldTodo(userInfo);
        createNewTodo(userInfo);
    }


    public UserGestationTodoResp getGestationTodoList(int userId) {
        UserGestationTodoResp userGestationTodoResp = new UserGestationTodoResp();
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        userGestationTodoResp.setEdcInterval(DateUtil.getIntervalOfCalendarDay(userInfo.getEdc() , new Date()));
        userGestationTodoResp.setTodoNotifySwitch(userInfo.getTodoNotifySwitch());
        List<TodoItem> todoItemList = new ArrayList<TodoItem>();
        TodoType currentTodoType = findLatestTodoType(userInfo.getEdc());
        while (currentTodoType != null) {
            todoItemList.add(transferTodoTypeToItem(userInfo.getEdc(), currentTodoType));
            currentTodoType = todoTypeMapper.findById(currentTodoType.getNextId());
        }
        userGestationTodoResp.setTodoList(todoItemList);
        return userGestationTodoResp;
    }


    public TodoDetailResp getGestationTodoDetail(int typeId) {
        TodoDetailResp todoDetailResp = new TodoDetailResp();
        List<TodoTypeDetail> typeDetailList = todoTypeDetailMapper.findByTypeId(typeId);
        if (!CollectionUtils.isEmpty(typeDetailList)) {
            todoDetailResp.setDetailItemList(typeDetailList.stream().map(typeDetail-> {
                TodoDetailItem todoDetailItem = new TodoDetailItem();
                todoDetailItem.setTitle(typeDetail.getTitle());
                todoDetailItem.setContent(typeDetail.getContent());
                return todoDetailItem;
            }).collect(Collectors.toList()));
        }
        return todoDetailResp;
    }


    /**
     * 打开提醒开关
     * @param userId
     */
    public void notifyOn(int userId) {
        userInfoMapper.updateTodoNotifySwitch(userId, TodoNotifySwitch.ON.getVal());
    }

    /**
     * 关闭提醒开关
     * @param userId
     */
    public void notifyOff(int userId) {
        userInfoMapper.updateTodoNotifySwitch(userId, TodoNotifySwitch.OFF.getVal());
    }


    private void clearOldTodo(UserInfo userInfo) {
        GestationTodo gestationTodo = new GestationTodo(userInfo.getUserId());
        gestationTodoDelayedTask.endTask(new DelayedMessage(new Date(), gestationTodo));
        todoLogMapper.deleteLogByUserId(userInfo.getUserId());
    }

    private void createNewTodo(UserInfo userInfo) {
        TodoType todoType = findLatestTodoType(userInfo.getEdc());
        if (todoType != null) {
            List<TodoTypeDetail> todoTypeDetailList = todoTypeDetailMapper.findByTypeId(todoType.getId());
            Map<String, String> data = new HashMap<>();
            Date sendTime = calSendTime(userInfo.getEdc(), todoType);
            int timeRemaining = DateUtil.getIntervalOfCalendarDay(sendTime, new Date());
            data.put("first", String.format(FIRST_TEMPLATE, timeRemaining, todoType.getTitle()));
            data.put("keyword1", DateUtil.format(sendTime));
            data.put("keyword2", "xxxx");
            data.put("remark", "点击查看本次产检更多注意事项吧~");

            GestationTodo todo = new GestationTodo(todoType.getId(), userInfo.getUserId(), userInfo.getOpenId(), data);
            TodoLog todoLog = createTodoLog(sendTime, todo);
            todoLogMapper.insertLog(todoLog);
            gestationTodoDelayedTask.put(sendTime, todo);
        }
    }

    private TodoType findLatestTodoType(Date edc) {
        Date doc = DateUtil.addDays(edc, -DAY_INTERVAL);
        int minTodoDay = DateUtil.getIntervalOfCalendarDay(new Date(), doc);
        TodoType todoType = todoTypeMapper.findByMainTypeIdAndMinTodoDay(TodoMainType.GESTATION.getType(), minTodoDay);
        return todoType;
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
        todoLog.setDataJson(JSONObject.toJSONString(todo.getData()));
        todoLog.setTypeId(todo.getTypeId());
        todoLog.setSendTime(sendTime);
        todoLog.setMainTypeId(TodoMainType.GESTATION.getType());
        return todoLog;
    }


    private TodoItem transferTodoTypeToItem(Date edc, TodoType todoType) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTypeId(todoType.getId());
        Date  birthInspectionDate = DateUtil.addDays(edc, -DAY_INTERVAL + todoType.getTodoDay());
        todoItem.setTodoDate(DateUtil.format(birthInspectionDate));
        todoItem.setTodoTitle(todoType.getTitle());
        return todoItem;
    }
}
