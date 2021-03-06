package com.momassistant.mapper.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhufeng on 2018/8/15.
 */
@Data
public class TodoType implements Serializable{
    private int id;
    private int nextId;
    private int preId;
    private int todoWeek;
    private int todoMonth;
    private String title;
    private int mainType;
}
