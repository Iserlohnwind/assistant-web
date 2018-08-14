package com.momassistant.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

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

    private int userType;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date edc;
}
