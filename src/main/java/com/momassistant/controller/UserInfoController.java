package com.momassistant.controller;

import com.momassistant.entity.Response;
import com.momassistant.entity.request.UserInfoReq;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhufeng on 2018/8/14.
 */
@RestController
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @RequestMapping("/userInfo/update")
    public Response<UserInfo> updateUserInfo(UserInfoReq userInfoReq) {
        return Response.success(userInfoService.updateUserInfo(userInfoReq));
    }

    @RequestMapping("/userInfo/get")
    public Response<UserInfo> getUserInfo(int userId) {
        return Response.success(userInfoService.getUserDetail(userId));
    }
}
