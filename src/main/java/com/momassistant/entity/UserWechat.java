package com.momassistant.entity;

import lombok.Data;

@Data
public class UserWechat {
    private int userId;
    private String wechatName;
    private String userName;
    private String userHeadPic;
    private String userRegion;
    private int gender;
    private int mobile;
}
