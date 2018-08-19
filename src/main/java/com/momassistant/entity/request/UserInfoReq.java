package com.momassistant.entity.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by Kris on 2018/8/14.
 */
@Data
public class UserInfoReq {
    private String wechatName;
    private String userName;
    private String userHeadPic;
    private String userRegion;
    private int gender;
    private String mobile;

    private int userType;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date edc;


    private List<BabyInfoReq> babyInfoList;

    public static void main(String[] args) {
        UserInfoReq userInfoReq = new UserInfoReq();
        userInfoReq.setWechatName("xxx");
        userInfoReq.setUserName("dddad");
        userInfoReq.setUserHeadPic("http://xxxx");
        userInfoReq.setUserRegion("安徽合肥");
        userInfoReq.setGender(1);
        userInfoReq.setGender(1);
        userInfoReq.setMobile("13811111111");
        userInfoReq.setUserType(1);
        System.out.println(JSONObject.toJSON(userInfoReq));
    }
}
