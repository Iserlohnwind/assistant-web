package com.momassistant.service;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.entity.response.TodoItem;
import com.momassistant.entity.response.UserGestationTodoResp;
import com.momassistant.enums.TodoMainType;
import com.momassistant.mapper.TodoLogMapper;
import com.momassistant.mapper.TodoTypeDetailMapper;
import com.momassistant.mapper.TodoTypeMapper;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoType;
import com.momassistant.mapper.model.TodoTypeDetail;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.message.DelayedTask;
import com.momassistant.message.GestationTodo;
import com.momassistant.message.Todo;
import com.momassistant.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private UserInfoMapper userInfoMapper;
    @Autowired
    @Qualifier("gestationTodoDelayedTask")
    private DelayedTask gestationTodoDelayedTask;

    @Autowired
    private TodoLogService todoLogService;

    /**
     * 修改孕期的时候初始化todo
     * @param userId
     */
    @Async
    public void initGestationTodo(int userId) {
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        Optional<TodoType> todoType = findLatestTodoType(userInfo.getEdc());
        if (todoType.isPresent()) {
            createTodo(userInfo, todoType.get());
        }
    }


    public UserGestationTodoResp getGestationTodoList(int userId) {
        UserGestationTodoResp userGestationTodoResp = new UserGestationTodoResp();
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        userGestationTodoResp.setEdcInterval(DateUtil.getIntervalOfCalendarDay(userInfo.getEdc() , new Date()));
        userGestationTodoResp.setTodoNotifySwitch(userInfo.getTodoNotifySwitch());
        List<TodoItem> todoItemList = new ArrayList<TodoItem>();
        Optional<TodoType> currentTodoType = findLatestTodoType(userInfo.getEdc());
        while (currentTodoType.isPresent()) {
            todoItemList.add(transferTodoTypeToItem(userInfo.getEdc(), currentTodoType.get()));
            currentTodoType = findTodoTypeById(currentTodoType.get().getNextId());
        }
        userGestationTodoResp.setTodoList(todoItemList);
        return userGestationTodoResp;
    }

    public void createTodo(UserInfo userInfo, TodoType todoType) {
        Map<String, String> data = new HashMap<>();
        Date todoDate = caculateTodoDate(userInfo.getEdc(), todoType);
        Date sendDate = caculateSendDate(todoDate);
        int timeRemaining = DateUtil.getIntervalOfCalendarDay(todoDate, new Date());
        data.put("first", String.format(FIRST_TEMPLATE, timeRemaining, todoType.getTitle()));
        data.put("keyword1", DateUtil.format(todoDate));
        data.put("remark", "点击查看本次产检更多注意事项吧~");
        List<TodoTypeDetail> todoTypeDetailList = todoTypeDetailMapper.findByTypeId(todoType.getId());
        todoTypeDetailList.stream().forEach(todoTypeDetail -> {
            if ("keyword2".equals(todoTypeDetail.getKeyword())) {
                data.put("keyword2", todoTypeDetail.getContent());
            }
        });

        GestationTodo todo = new GestationTodo(todoType.getId(), userInfo.getUserId(), userInfo.getOpenId(), data);
        todoLogService.refreshTodoLog(sendDate, todo);
        gestationTodoDelayedTask.put(sendDate, todo);
    }

    private Optional<TodoType> findLatestTodoType(Date edc) {
        Date doc = DateUtil.addDays(edc, -DAY_INTERVAL);
        int minTodoWeek = DateUtil.getIntervalOfCalendarDay(new Date(), doc) / 7 + 1;
        TodoType todoType = todoTypeMapper.findByMainTypeIdAndMinTodoWeek(TodoMainType.GESTATION.getType(), minTodoWeek);
        return Optional.ofNullable(todoType);
    }


    private Optional<TodoType> findTodoTypeById(int type) {
        TodoType todoType = todoTypeMapper.findById(type);
        return Optional.ofNullable(todoType);
    }

    private Date caculateTodoDate(Date edc, TodoType todoType) {
        Date sendTime = DateUtil.addDays(edc, -DAY_INTERVAL + todoType.getTodoWeek() * 7);
        return sendTime;
    }

    private Date caculateSendDate(Date todoDate) {
        Date sendDate = DateUtil.addDays(todoDate, -6);
        Date now = new Date();
        while (sendDate.before(now)) {
            sendDate = DateUtil.addDays(sendDate, 1);
        }
        return sendDate;
    }


    private TodoItem transferTodoTypeToItem(Date edc, TodoType todoType) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTypeId(todoType.getId());
        Date todoDate = caculateTodoDate(edc, todoType);
        todoItem.setTodoDate(DateUtil.format(todoDate));
        todoItem.setTodoTitle(todoType.getTitle());
        return todoItem;
    }
}
