package com.momassistant.mapper.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhufeng on 2018/8/15.
 */
@Data
public class TodoLog implements Serializable{
    private int id;
    private int userId;
    private String opendId;
    private int typeId;
    private int mainTypeId;
    private String title;
    private String content;
    private String url;
    private Date sendTime;
    private int node;

    private int babyId;
    private String babyName;


}
