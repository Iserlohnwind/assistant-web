package com.momassistant.service;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.entity.response.*;
import com.momassistant.enums.GenderType;
import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.enums.WechatMsgTemplate;
import com.momassistant.mapper.*;
import com.momassistant.mapper.model.*;
import com.momassistant.message.*;
import com.momassistant.utils.DateUtil;
import com.momassistant.wechat.GestationMessageData;
import com.momassistant.wechat.LactationMessageData;
import com.momassistant.wechat.WeiXinSendValue;
import com.momassistant.wechat.WeiXinTemplate;
import jodd.datetime.JDateTime;
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
    public void initLactationTodo(List<BabyInfo> oldBabyInfoList, int userId) {
        UserInfo userInfo = userInfoMapper.getUserDetail(userId);
        List<BabyInfo> babyInfoList = babyInfoMapper.findByUserId(userId);


        if (!CollectionUtils.isEmpty(oldBabyInfoList)) {
            oldBabyInfoList.stream().forEach(babyInfo -> {
                lactationTodoDelayedTask.remove(new LactationTodo(userId, babyInfo.getBabyId()));
            });
        }

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
        if (userInfo == null) {
            return null;
        }
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
                babyTodoResp.setBabyBirth(getBabyBirth(babyInfo.getBabyBirthday()));
                babyTodoResp.setTodoItemList(todoItemList);
                return babyTodoResp;
            }).collect(Collectors.toList()));
        }
        return userLactationTodoResp;
    }

    public void createTodo(TodoType todoType, UserInfo userInfo, BabyInfo babyInfo) {
        if (todoType != null) {
            Date todoDate = getTodoDay(babyInfo.getBabyBirthday(), todoType.getTodoMonth());
            Date sendDate = getSendDate(todoDate);
            WeiXinTemplate weiXinTemplate = buildWeixinTemplate(userInfo, babyInfo, todoType);
            LactationTodo todo = new LactationTodo(todoType.getId(), userInfo.getUserId(), userInfo.getOpenId(), weiXinTemplate, babyInfo);
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

    private TodoItem transferTodoTypeToItem(Date birthDate, TodoType todoType) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTypeId(todoType.getId());
        Date todoDate = getTodoDay(birthDate, todoType.getTodoMonth());
        todoItem.setTodoDate(DateUtil.format(todoDate));
        todoItem.setTodoTitle(todoType.getTitle());
        return todoItem;
    }


    private WeiXinTemplate buildWeixinTemplate(UserInfo userInfo, BabyInfo babyInfo, TodoType todoType) {
        WeiXinTemplate<LactationMessageData> weiXinTemplate = new WeiXinTemplate<LactationMessageData>();
        weiXinTemplate.setTemplate_id(WechatMsgTemplate.LACTATION_MSG.getTemplateId());
        weiXinTemplate.setTouser(userInfo.getPaOpenId());
        LactationMessageData lactationMessageData = new LactationMessageData();

        Date todoDate = getTodoDay(babyInfo.getBabyBirthday(), todoType.getTodoMonth());
        lactationMessageData.setFirst(new WeiXinSendValue("尊敬的家长,您好!您的孩子近日需要接种疫苗,请及时安排您的孩子到指定接种点进行接种!", "#888888"));
        lactationMessageData.setKeyword1(new WeiXinSendValue(String.format("姓名：%s", babyInfo.getBabyName()), "#888888"));
        lactationMessageData.setKeyword2(new WeiXinSendValue(String.format("性别：%s", GenderType.getByType(babyInfo.getBabyGender()).getGenderTxt()), "#888888"));
        lactationMessageData.setKeyword3(new WeiXinSendValue(DateUtil.format(todoDate), "#888888"));
        lactationMessageData.setKeyword4(new WeiXinSendValue(String.format("计划接种疫苗：%s", todoType.getTitle()), "#888888"));

        List<TodoTypeDetail> todoTypeDetailList = commonTodoService.findByTypeId(todoType.getId());
        todoTypeDetailList.stream().forEach(todoTypeDetail -> {
            if ("remark".equals(todoTypeDetail.getKeyword())) {
                lactationMessageData.setRemark(new WeiXinSendValue(String.format("注意事项：%s", todoTypeDetail.getContent()), "#888888"));
            }
        });

        weiXinTemplate.setData(lactationMessageData);
        return weiXinTemplate;
    }

    private BabyBirth getBabyBirth(Date birthDate) {
        BabyBirth babyBirth = new BabyBirth();
        int monthDiff = DateUtil.getBabyMonthDiff(birthDate);
        if (monthDiff >= 12) {
            babyBirth.setBirthNum(monthDiff / 12);
            babyBirth.setBirthUnit("岁");

        } else if (monthDiff > 1) {
            babyBirth.setBirthNum(monthDiff);
            babyBirth.setBirthUnit("月");
        } else {
            int birthDays = DateUtil.getIntervalOfCalendarDay(new Date(), birthDate);

            babyBirth.setBirthNum(birthDays);
            babyBirth.setBirthUnit("天");
        }
        return babyBirth;
    }



    private Date getTodoDay(Date birthDate, int todoMonth) {
        if (todoMonth == 0) {
            return birthDate;
        }
        JDateTime birthJDateTime = new JDateTime(birthDate);
        birthJDateTime.addMonth(todoMonth);
        birthJDateTime.addDay(1);
        Date dayFrom = birthJDateTime.convertToDate();
        if (dayFrom.before(DateUtil.getTomorrow())) {
            dayFrom = DateUtil.getTomorrow();
        }
        return dayFrom;
    }

    private Date getSendDate(Date todoDate) {
        Date sendDate = DateUtil.addDays(todoDate, -3);
        Date now = new Date();
        while (sendDate.before(now)) {
            sendDate = DateUtil.addDays(sendDate, 1);
        }

        if (sendDate.after(todoDate)) {
            sendDate = todoDate;
        }
        return sendDate;
    }
//
//    public static void main(String[] args) {
//        Date birthDate = DateUtil.getTomorrow();
//        for (int j = 1;j<=360;j++) {
//            birthDate = DateUtil.addDays(birthDate, -1);
//            int minTodoMonth;
//            if (DateUtil.lessThan24Hour(birthDate)) {
//                minTodoMonth = 0;
//            } else {
//                minTodoMonth = Math.max(DateUtil.getBabyMonthDiff(birthDate), 1);
//            }
//            System.out.println();
//
//            System.out.println(String.format("todo:%s,send:%s",DateUtil.format(getTodoDaysta(birthDate, minTodoMonth)), DateUtil.format(getSendDate(getTodoDaysta(birthDate, minTodoMonth)))));
//
//
//        }
//    }
//
//    public static void main1(String[] args) {
//        Date birthDate = DateUtil.getTomorrow();
//
//        for (int j = 1;j<=30;j++) {
//            birthDate = DateUtil.addDays(birthDate, -1);
//            for (int i = 0; i <= 12; i++) {
//                Date dayFrom = getTodoDayFrom(birthDate, i);
//                Date dayTo = getTodoDayTo(birthDate, i);
//                System.out.println(String.format("生日:%s,from:%s,to:%s", DateUtil.format(birthDate), DateUtil.format(dayFrom), DateUtil.format(dayTo)));
//            }
//
//            System.out.println();
//        }
//    }


}
