package com.momassistant.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date edc;
}
