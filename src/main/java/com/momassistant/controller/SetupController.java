package com.momassistant.controller;

import com.momassistant.annotations.UserValidate;
import com.momassistant.entity.Response;
import com.momassistant.entity.request.BabyInfoReq;
import com.momassistant.entity.request.UserInfoReq;
import com.momassistant.service.BabyInfoService;
import com.momassistant.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Kris on 2018/8/14.
 */
@RestController
public class SetupController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    BabyInfoService babyInfoService;

    /**
     * 保存用户微信信息
     *
     * @param userInfoReq
     * @return
     */
    @RequestMapping("setupWechatInfo")
    @UserValidate
    public Response<Boolean> setupUserWechatInfo(UserInfoReq userInfoReq) {
        userInfoService.updateUserWechatInfo(userInfoReq);
        return Response.success(Boolean.TRUE);
    }

    /**
     * 保存用户相关信息及设置预产期
     *
     * @param userInfoReq
     * @return
     */
    @RequestMapping("setupPregancyInfo")
    @UserValidate
    public Response<Boolean> setupPregancyInfo(UserInfoReq userInfoReq) {
        userInfoService.updatePregancyInfo(userInfoReq);
        return Response.success(Boolean.TRUE);
    }

    /**
     * 保存用户相关信息及设置宝宝信息
     *
     * @param userInfoReq
     * @param babyInfoReqList
     * @return
     */
    @RequestMapping("setupBabyInfo")
    @UserValidate
    public Response<Boolean> setupBabyInfo(UserInfoReq userInfoReq, List<BabyInfoReq> babyInfoReqList) {
        userInfoService.updatePregancyInfo(userInfoReq);
        babyInfoService.updateBabyInfo(userInfoReq, babyInfoReqList);
        return Response.success(Boolean.TRUE);
    }

}
