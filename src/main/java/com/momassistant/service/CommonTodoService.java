package com.momassistant.service;

import com.momassistant.entity.response.TodoDetailItem;
import com.momassistant.entity.response.TodoDetailResp;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.mapper.TodoTypeDetailMapper;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.model.TodoTypeDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhufeng on 2018/8/20.
 */
@Service
public class CommonTodoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private TodoTypeDetailMapper todoTypeDetailMapper;

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

    public List<TodoTypeDetail> findByTypeId(int typeId) {
        return todoTypeDetailMapper.findByTypeId(typeId);
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



    public boolean checkTodoNotifySwitchOn(int userId) {
        Integer todoNotifySwitch = userInfoMapper.getTodoNotifySwitch(userId);
        int _todoNotifyWitch = todoNotifySwitch != null ? todoNotifySwitch : 0;
        return _todoNotifyWitch == TodoNotifySwitch.ON.getVal();
    }



}
