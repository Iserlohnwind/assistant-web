package com.momassistant.mapper.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Data
public class UserInfo implements Serializable{
    private int userId;
    private String openId;
    private String wechatName;
    private String userName;
    private String userHeadPic;
    private String userRegion;
    private int gender;
    private String mobile;
    private int userType;
    private Date edc;
    private int todoNotifySwitch;
}
