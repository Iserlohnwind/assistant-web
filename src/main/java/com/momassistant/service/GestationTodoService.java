package com.momassistant.service;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.entity.response.TodoItem;
import com.momassistant.entity.response.UserGestationTodoResp;
import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.WechatMsgTemplate;
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
import com.momassistant.wechat.GestationMessageData;
import com.momassistant.wechat.WeiXinSendValue;
import com.momassistant.wechat.WeiXinTemplate;
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
        userGestationTodoResp.setPregnancyTime(caculatePregnancyTime(userGestationTodoResp.getEdcInterval()));
        userGestationTodoResp.setTodoNotifySwitch(userInfo.getTodoNotifySwitch());
        userGestationTodoResp.setUserHeadPic(userInfo.getUserHeadPic());
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
        Date todoDate = caculateTodoDate(userInfo.getEdc(), todoType);
        Date sendDate = caculateSendDate(todoDate);
        WeiXinTemplate<GestationMessageData> weiXinTemplate = buildWeixinTemplate(userInfo, todoType);
        GestationTodo todo = new GestationTodo(todoType.getId(), userInfo.getUserId(), userInfo.getOpenId(), weiXinTemplate);
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
        List<TodoTypeDetail> todoTypeDetailList = todoTypeDetailMapper.findByTypeId(todoType.getId());
        todoTypeDetailList.stream().forEach(todoTypeDetail -> {
            if ("keyword2".equals(todoTypeDetail.getKeyword())) {
                todoItem.setAtttention(todoTypeDetail.getContent());
            }
        });
        return todoItem;
    }

    private String caculatePregnancyTime(int edcInterval) {
        StringBuilder pregnancyTime = new StringBuilder("已怀孕 ");
        int pregnancyDay = DAY_INTERVAL - edcInterval;
        int pregnancyWeek = pregnancyDay / 7;
        pregnancyDay = pregnancyDay % 7;
        if (pregnancyWeek > 0) {
            pregnancyTime.append(pregnancyWeek + "周+");
        }
        if (pregnancyDay > 0) {
            pregnancyTime.append(pregnancyDay + "天+");
        }
        return pregnancyTime.substring(0, pregnancyTime.length() - 1);
    }

    private WeiXinTemplate buildWeixinTemplate(UserInfo userInfo, TodoType todoType) {
        WeiXinTemplate<GestationMessageData> weiXinTemplate = new WeiXinTemplate<GestationMessageData>();
        weiXinTemplate.setTemplate_id(WechatMsgTemplate.GESTATION_MSG.getTemplateId());
        weiXinTemplate.setTouser(userInfo.getPaOpenId());
        GestationMessageData gestationMessageData = new GestationMessageData();
        Date todoDate = caculateTodoDate(userInfo.getEdc(), todoType);
        Date sendDate = caculateSendDate(todoDate);
        int timeRemaining = DateUtil.getIntervalOfCalendarDay(todoDate, new Date());
        gestationMessageData.setFirst(new WeiXinSendValue(String.format(FIRST_TEMPLATE, timeRemaining, todoType.getTitle()), "#888888"));
        gestationMessageData.setKeyword1(new WeiXinSendValue(DateUtil.format(todoDate), "#888888"));
        gestationMessageData.setRemark(new WeiXinSendValue("点击查看本次产检更多注意事项吧~", "#888888"));
        List<TodoTypeDetail> todoTypeDetailList = todoTypeDetailMapper.findByTypeId(todoType.getId());
        todoTypeDetailList.stream().forEach(todoTypeDetail -> {
            if ("keyword2".equals(todoTypeDetail.getKeyword())) {
                gestationMessageData.setKeyword2(new WeiXinSendValue(todoTypeDetail.getContent(), "#888888"));
            }
        });
        weiXinTemplate.setData(gestationMessageData);
        return weiXinTemplate;
    }
}
