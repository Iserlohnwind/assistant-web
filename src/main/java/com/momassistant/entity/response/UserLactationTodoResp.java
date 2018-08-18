package com.momassistant.entity.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/17.
 */
@Data
public class UserLactationTodoResp implements Serializable{
    /**
     * 距离预产期时间
     */
    private int edcInterval;

    /**
     * 提醒开关
     */
    private int todoNotifySwitch;

    private List<GestationTodoItem> todoList;
}
