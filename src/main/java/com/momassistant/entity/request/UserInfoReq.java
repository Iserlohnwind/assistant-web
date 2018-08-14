package com.momassistant.entity.request;

import lombok.Data;

/**
 * Created by Kris on 2018/8/14.
 */
@Data
public class UserInfoReq {
    private int userId;
    private String wechatName;
    private String userName;
    private String userHeadPic;
    private String userRegion;
    private int gender;
    private int mobile;
}
