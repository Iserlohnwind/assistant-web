package com.momassistant.mapper.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Data
public class WechatInfo implements Serializable{
    private int userId;
    private String wechatId;
    private int status;
}
