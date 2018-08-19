package com.momassistant.controller;

import com.momassistant.annotations.UserValidate;
import com.momassistant.entity.Response;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.service.UserInfoService;
import com.momassistant.utils.HtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhufeng on 2018/8/14.
 */
@RestController
@Api("用户相关api")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
//    @RequestMapping("/userInfo/update")
//    @UserValidate
//    public Response<UserInfo> updateUserInfo(UserInfoReq userInfoReq) {
//        return Response.success(userInfoService.updateUserInfo(userInfoReq));
//    }

    @ApiOperation(value = "根据userid查询用户信息", notes = "根据userid查询用户信息")
    @RequestMapping("/userInfo/get")
    @UserValidate
    public Response<UserInfo> getUserInfo() {
        return Response.success(userInfoService.getUserDetail(HtmlUtil.getUserId()));
    }
}
