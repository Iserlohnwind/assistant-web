package com.momassistant.controller;

import com.momassistant.annotations.UserValidate;
import com.momassistant.entity.Response;
import com.momassistant.entity.request.SetupLactationInfoReq;
import com.momassistant.entity.request.SetupPregancyInfoReq;
import com.momassistant.entity.request.SetupWechatInfoReq;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.service.BabyInfoService;
import com.momassistant.service.GestationTodoService;
import com.momassistant.service.LactationTodoService;
import com.momassistant.service.UserInfoService;
import com.momassistant.utils.HtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhufeng on 2018/8/14.
 */
@RestController
@Api("用户相关api")
@RequestMapping("user")
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    BabyInfoService babyInfoService;

    @Autowired
    GestationTodoService gestationTodoService;

    @Autowired
    LactationTodoService lactationTodoService;


    @ApiOperation(value = "根据userid查询用户信息", notes = "根据userid查询用户信息")
    @RequestMapping("getUserInfo")
    @UserValidate
    public Response<UserInfo> getUserInfo() {
        return Response.success(userInfoService.getUserDetail(HtmlUtil.getUserId()));
    }



    /**
     * 保存用户微信信息
     *
     * @param setupWechatInfoReq
     * @return
     */
    @ApiOperation(value = "保存用户微信信息", notes = "保存用户微信信息", httpMethod = "POST")
    @ApiImplicitParam(name = "setupWechatInfoReq", value = "用户详细实体user", required = true, dataType = "SetupWechatInfoReq")
    @RequestMapping(value = "updateWechatInfo", method = {RequestMethod.POST})
    @UserValidate
    public Response<Boolean> setupUserWechatInfo(@RequestBody SetupWechatInfoReq setupWechatInfoReq) {
        userInfoService.updateUserWechatInfo(setupWechatInfoReq);
        return Response.success(Boolean.TRUE);
    }

    /**
     * 保存用户相关信息及设置预产期
     *
     * @param setupPregancyInfoReq
     * @return
     */
    @ApiOperation(value = "保存用户相关信息及设置预产期", notes = "保存用户相关信息及设置预产期", httpMethod = "POST")
    @ApiImplicitParam(name = "setupPregancyInfoReq", value = "用户详细实体user", required = true, dataType = "SetupPregancyInfoReq")
    @RequestMapping(value = "updateGestationalInfo", method = {RequestMethod.POST})
    @UserValidate
    public Response<Boolean> updateGestationalInfo(@RequestBody SetupPregancyInfoReq setupPregancyInfoReq) {
        userInfoService.updatePregancyInfo(setupPregancyInfoReq);
        gestationTodoService.initGestationTodo(HtmlUtil.getUserId());
        return Response.success(Boolean.TRUE);
    }

    /**
     * 保存用户相关信息及设置宝宝信息
     *
     * @param setupLactationInfoReq
     * @return
     */
    @ApiOperation(value = "保存用户相关信息及设置宝宝信息", notes = "保存用户相关信息及设置宝宝信息", httpMethod = "POST")
    @ApiImplicitParam(name = "setupLactationInfoReq", value = "用户详细实体user", required = true, dataType = "SetupLactationInfoReq")
    @RequestMapping(value = "updateLactationalInfo", method = {RequestMethod.POST})
    @UserValidate
    public Response<Boolean> updateLactationalInfo(@RequestBody SetupLactationInfoReq setupLactationInfoReq) {
        userInfoService.updateLactationInfo(setupLactationInfoReq);
        babyInfoService.updateBabyInfo(setupLactationInfoReq.getBabyInfoList());
        lactationTodoService.initLactationTodo(HtmlUtil.getUserId());
        return Response.success(Boolean.TRUE);
    }
}
