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
    private String openId;
    private int typeId;
    private int mainTypeId;
    private String dataJson;
    private String url;
    private Date sendTime;
    private int node;

    private int babyId;


}
