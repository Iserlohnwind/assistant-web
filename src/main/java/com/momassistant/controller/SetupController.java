package com.momassistant.controller;

import com.momassistant.entity.Response;
import com.momassistant.entity.UserInfoDTO;
import com.momassistant.entity.request.BabyInfoReq;
import com.momassistant.entity.request.UserInfoReq;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by Kris on 2018/8/14.
 */
@RestController
public class SetupController {

    /**
     * 保存用户微信信息
     * @param userInfoReq
     * @return
     */
    @RequestMapping("setupWechatInfo")
    public Response<Boolean> setupUserWechatInfo(UserInfoReq userInfoReq){

        return Response.success(Boolean.TRUE);
    }

    /**
     * 保存用户相关信息及设置预产期
     * @param userInfoReq
     * @param edcDate
     * @return
     */
    @RequestMapping("setupPregancyInfo")
    public Response<Boolean> setupPregancyInfo(UserInfoReq userInfoReq, Date edcDate){

        return Response.success(Boolean.TRUE);
    }

    /**
     * 保存用户相关信息及设置宝宝信息
     * @param userInfoReq
     * @param babyInfoReqList
     * @return
     */
    @RequestMapping("setupBabyInfo")
    public Response<Boolean> setupBabyInfo(UserInfoReq userInfoReq,List<BabyInfoReq> babyInfoReqList){

        return Response.success(Boolean.TRUE);
    }

}
