package com.momassistant.entity.response;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhufeng on 2018/8/17.
 */
@Data
public class TodoItem implements Serializable{
    /**
     * 产检日期
     */
    private String todoDate;

    /**
     * 提醒标题
     */
    private String todoTitle;

    /**
     * 提醒类型
     */
    private int typeId;

    private String atttention;
}
