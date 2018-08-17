package com.momassistant.service;

import com.momassistant.entity.response.GestationTodoDetailItem;
import com.momassistant.entity.response.GestationTodoItem;
import com.momassistant.entity.response.UserGestationTodoDetailResp;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zhufeng on 2018/8/16.
 */
@Service
public class GestationTodoService {
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
        userGestationTodoResp.setEdcInterval((int) ((userInfo.getEdc().getTime() - new Date().getTime()) / (1000*3600*24)));
        userGestationTodoResp.setTodoNotifySwitch(userInfo.getTodoNotifySwitch());
        List<GestationTodoItem> gestationTodoItemList = new ArrayList<GestationTodoItem>();
        TodoType currentTodoType = findLatestTodoType(userInfo.getEdc());
        while (currentTodoType != null) {
            gestationTodoItemList.add(transferTodoTypeToItem(userInfo.getEdc(), currentTodoType));
            currentTodoType = todoTypeMapper.findById(currentTodoType.getNextId());
        }
        userGestationTodoResp.setTodoList(gestationTodoItemList);
        return userGestationTodoResp;
    }


    public UserGestationTodoDetailResp getGestationTodoDetail(int typeId) {
        UserGestationTodoDetailResp userGestationTodoDetailResp = new UserGestationTodoDetailResp();
        List<TodoTypeDetail> typeDetailList = todoTypeDetailMapper.findByTypeId(typeId);
        if (!CollectionUtils.isEmpty(typeDetailList)) {
            userGestationTodoDetailResp.setDetailItemList(typeDetailList.stream().map(typeDetail-> {
                GestationTodoDetailItem gestationTodoDetailItem = new GestationTodoDetailItem();
                gestationTodoDetailItem.setTitle(typeDetail.getTitle());
                gestationTodoDetailItem.setContent(typeDetail.getContent());
                return gestationTodoDetailItem;
            }).collect(Collectors.toList()));
        }
        return userGestationTodoDetailResp;
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
        todoLogMapper.deleteLog(userInfo.getUserId());
    }

    private void createNewTodo(UserInfo userInfo) {
        TodoType todoType = findLatestTodoType(userInfo.getEdc());
        if (todoType != null) {
            List<TodoTypeDetail> todoTypeDetailList = todoTypeDetailMapper.findByTypeId(todoType.getId());
            GestationTodo todo = new GestationTodo(todoType.getId(), userInfo.getUserId(), userInfo.getOpenId(), todoType.getTitle(), todoTypeDetailList);
            Date sendTime = calSendTime(userInfo.getEdc(), todoType);
            TodoLog todoLog = createTodoLog(sendTime, todo);
            todoLogMapper.insertLog(todoLog);
            gestationTodoDelayedTask.put(sendTime, todo);
        }
    }

    private TodoType findLatestTodoType(Date edc) {
        Date doc = DateUtil.addDays(edc, -DAY_INTERVAL);
        int minTodoDay = (int) ((new Date().getTime() - doc.getTime()) / (1000*3600*24));
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
        todoLog.setTitle(todo.getTitle());
        todoLog.setContent(todo.getContent());
        todoLog.setTypeId(todo.getTypeId());
        todoLog.setSendTime(sendTime);
        todoLog.setMainTypeId(TodoMainType.GESTATION.getType());
        return todoLog;
    }


    private GestationTodoItem transferTodoTypeToItem(Date edc, TodoType todoType) {
        GestationTodoItem gestationTodoItem = new GestationTodoItem();
        gestationTodoItem.setTypeId(todoType.getId());
        Date  birthInspectionDate = DateUtil.addDays(edc, -DAY_INTERVAL + todoType.getTodoDay());
        gestationTodoItem.setBirthInspectionDate(DateUtil.format(birthInspectionDate));
        gestationTodoItem.setTodoTitle(todoType.getTitle());
        return gestationTodoItem;
    }
}
