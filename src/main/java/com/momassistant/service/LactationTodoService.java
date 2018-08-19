package com.momassistant.service;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.entity.response.*;
import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.mapper.*;
import com.momassistant.mapper.model.*;
import com.momassistant.message.*;
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
public class LactationTodoService {
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
    @Autowired
    @Qualifier("lactationTodoDelayedTask")
    private DelayedTask lactationTodoDelayedTask;

    /**
     * 修改孕期的时候初始化todo
     * @param userId
     */
    @Async
    public void initLactationTodo(int userId) {
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        List<BabyInfo> babyInfoList = babyInfoMapper.findByUserId(userId);
        if (!CollectionUtils.isEmpty(babyInfoList)) {
            babyInfoList.stream().forEach(babyInfo -> {
                clearOldTodo(babyInfo);
                createNewTodo(userInfo, babyInfo);
            });
        }

    }


    public UserLactationTodoResp getTodoList(int userId) {
        UserLactationTodoResp userLactationTodoResp = new UserLactationTodoResp();
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        userLactationTodoResp.setTodoNotifySwitch(userInfo.getTodoNotifySwitch());
        List<BabyInfo> babyInfoList = babyInfoMapper.findByUserId(userId);
        if (!CollectionUtils.isEmpty(babyInfoList)) {
            userLactationTodoResp.setBabyList(babyInfoList.stream().map(babyInfo -> {
                BabyTodoResp babyTodoResp = new BabyTodoResp();
                List<TodoItem> todoItemList = new ArrayList<TodoItem>();
                TodoType currentTodoType = findLatestTodoType(babyInfo.getBabyBirthday());
                while (currentTodoType != null) {
                    todoItemList.add(transferTodoTypeToItem(userInfo.getEdc(), currentTodoType));
                    currentTodoType = todoTypeMapper.findById(currentTodoType.getNextId());
                }
                babyTodoResp.setBabyName(babyInfo.getBabyName());
                babyTodoResp.setBirthDate(DateUtil.format(babyInfo.getBabyBirthday()));
                babyTodoResp.setTodoItemList(todoItemList);
                return babyTodoResp;
            }).collect(Collectors.toList()));
        }
        return userLactationTodoResp;
    }


    public TodoDetailResp getTodoDetail(int typeId) {
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


    private void clearOldTodo(BabyInfo babyInfo) {
        LactationTodo lactationTodo = new LactationTodo(babyInfo.getBabyId());
        lactationTodoDelayedTask.endTask(new DelayedMessage(new Date(), lactationTodo));
        todoLogMapper.deleteLogByBabyId(babyInfo.getBabyId());
    }

    private void createNewTodo(UserInfo userInfo, BabyInfo babyInfo) {
        TodoType todoType = findLatestTodoType(babyInfo.getBabyBirthday());
        if (todoType != null) {
            List<TodoTypeDetail> todoTypeDetailList = todoTypeDetailMapper.findByTypeId(todoType.getId());


            Map<String, String> data = new HashMap<>();
            Date sendTime = calSendTime(babyInfo.getBabyBirthday(), todoType);
            data.put("first", "尊敬的家长,您好!您的孩子今日需要接种疫苗,请及时安排您的孩子到指定接种点进行接种!");
            data.put("keyword1", String.format("姓名：%s", babyInfo.getBabyName()));
            data.put("keyword2", String.format("性别：%s", babyInfo.getBabyGender()));
            data.put("keyword3", DateUtil.format(sendTime));
            data.put("keyword4", String.format("计划接种疫苗：%s", "xx"));
            data.put("remark", "注意事项：%s");

            LactationTodo todo = new LactationTodo(todoType.getId(), userInfo.getUserId(), userInfo.getOpenId(), data, babyInfo);
            TodoLog todoLog = createTodoLog(sendTime, todo);
            todoLogMapper.insertLog(todoLog);
            lactationTodoDelayedTask.put(sendTime, todo);
        }
    }

    private TodoType findLatestTodoType(Date birthDate) {
        int minTodoDay = DateUtil.getIntervalOfCalendarDay(new Date(), birthDate);
        TodoType todoType = todoTypeMapper.findByMainTypeIdAndMinTodoDay(TodoMainType.Lactation.getType(), minTodoDay);
        return todoType;
    }

    private Date calSendTime(Date birthDate, TodoType todoType) {
        Date sendTime = DateUtil.addDays(birthDate, todoType.getTodoDay());
        return sendTime;
    }


    private TodoLog createTodoLog(Date sendTime, Todo todo) {
        //新提醒入库
        TodoLog todoLog = new TodoLog();
        todoLog.setUserId(todo.getUserId());
        todoLog.setOpenId(todo.getOpenId());
        todoLog.setDataJson(JSONObject.toJSONString(todo.getData()));
        todoLog.setUrl("");
        todoLog.setTypeId(todo.getTypeId());
        todoLog.setSendTime(sendTime);
        todoLog.setMainTypeId(TodoMainType.GESTATION.getType());
        return todoLog;
    }


    private TodoItem transferTodoTypeToItem(Date birthDate, TodoType todoType) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTypeId(todoType.getId());
        Date  todoDate = DateUtil.addDays(birthDate, todoType.getTodoDay());
        todoItem.setTodoDate(DateUtil.format(todoDate));
        todoItem.setTodoTitle(todoType.getTitle());
        return todoItem;
    }
}
