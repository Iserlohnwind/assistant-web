package com.momassistant.service;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.entity.response.*;
import com.momassistant.enums.GenderType;
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
    private TodoLogService todoLogService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BabyInfoMapper babyInfoMapper;
    @Autowired
    @Qualifier("lactationTodoDelayedTask")
    private DelayedTask lactationTodoDelayedTask;

    @Autowired
    private CommonTodoService commonTodoService;

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
                TodoType todoType = findLatestTodoType(babyInfo.getBabyBirthday());
                createTodo(todoType, userInfo, babyInfo);
            });
        }

    }


    public UserLactationTodoResp getTodoList(int userId) {
        UserLactationTodoResp userLactationTodoResp = new UserLactationTodoResp();
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        userLactationTodoResp.setTodoNotifySwitch(userInfo.getTodoNotifySwitch());
        userLactationTodoResp.setUserHeadPic(userInfo.getUserHeadPic());
        List<BabyInfo> babyInfoList = babyInfoMapper.findByUserId(userId);
        if (!CollectionUtils.isEmpty(babyInfoList)) {
            userLactationTodoResp.setBabyList(babyInfoList.stream().map(babyInfo -> {
                BabyTodoResp babyTodoResp = new BabyTodoResp();
                List<TodoItem> todoItemList = new ArrayList<TodoItem>();
                TodoType currentTodoType = findLatestTodoType(babyInfo.getBabyBirthday());
                while (currentTodoType != null) {
                    todoItemList.add(transferTodoTypeToItem(babyInfo.getBabyBirthday(), currentTodoType));
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

    public void createTodo(TodoType todoType, UserInfo userInfo, BabyInfo babyInfo) {
        if (todoType != null) {
            Map<String, String> data = new HashMap<>();
            Date todoDate = caculateTodoDate(babyInfo.getBabyBirthday(), todoType);
            Date sendDate = caculateSendDate(todoDate);
            data.put("first", "尊敬的家长,您好!您的孩子今日需要接种疫苗,请及时安排您的孩子到指定接种点进行接种!");
            data.put("keyword1", String.format("姓名：%s", babyInfo.getBabyName()));
            data.put("keyword2", String.format("性别：%s", GenderType.getByType(babyInfo.getBabyGender()).getGenderTxt()));
            data.put("keyword3", DateUtil.format(todoDate));
            data.put("keyword4", String.format("计划接种疫苗：%s", todoType.getTitle()));

            List<TodoTypeDetail> todoTypeDetailList = commonTodoService.findByTypeId(todoType.getId());
            todoTypeDetailList.stream().forEach(todoTypeDetail -> {
                if ("remark".equals(todoTypeDetail.getKeyword())) {
                    data.put("remark", String.format("注意事项：%s", todoTypeDetail.getContent()));
                }
            });
            LactationTodo todo = new LactationTodo(todoType.getId(), userInfo.getUserId(), userInfo.getOpenId(), data, babyInfo);
            todoLogService.refreshTodoLog(sendDate, todo);
            lactationTodoDelayedTask.put(sendDate, todo);
        }
    }

    private TodoType findLatestTodoType(Date birthDate) {
        int minTodoMonth;
        if (DateUtil.lessThan24Hour(birthDate)) {
            minTodoMonth = 0;
        }
        else {
            minTodoMonth = Math.max(DateUtil.getBabyMonthDiff(birthDate), 1);
        }
        TodoType todoType = todoTypeMapper.findByMainTypeIdAndMinTodoMonth(TodoMainType.Lactation.getType(), minTodoMonth);
        return todoType;
    }


    private Date caculateTodoDate(Date birthDate, TodoType todoType) {
        Date todoDate = DateUtil.addDays(DateUtil.addMonths(birthDate, todoType.getTodoMonth()), 1);
        return todoDate;
    }

    private Date caculateSendDate(Date todoDate) {
        Date sendDate = DateUtil.addDays(todoDate, -6);
        Date now = new Date();
        while (sendDate.before(now)) {
            sendDate = DateUtil.addDays(sendDate, 1);
        }
        return sendDate;
    }


    private TodoItem transferTodoTypeToItem(Date birthDate, TodoType todoType) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTypeId(todoType.getId());
        Date todoDate = caculateTodoDate(birthDate, todoType);
        todoItem.setTodoDate(DateUtil.format(todoDate));
        todoItem.setTodoTitle(todoType.getTitle());
        return todoItem;
    }


}
