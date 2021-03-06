package com.momassistant.entity.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/17.
 */
@Data
public class UserGestationTodoResp implements Serializable{
    /**
     * 距离预产期时间
     */
    private int edcInterval;

    private String pregnancyTime;

    /**
     * 提醒开关
     */
    private int todoNotifySwitch;

    private String userHeadPic;

    private List<TodoItem> todoList;
}
