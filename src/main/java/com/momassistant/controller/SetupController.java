package com.momassistant.controller;

import com.momassistant.annotations.UserValidate;
import com.momassistant.entity.Response;
import com.momassistant.entity.request.*;
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

import java.util.List;

/**
 * Created by Kris on 2018/8/14.
 */
@RestController
@Api("信息编辑api")
public class SetupController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    BabyInfoService babyInfoService;

    @Autowired
    GestationTodoService gestationTodoService;

    @Autowired
    LactationTodoService lactationTodoService;
    /**
     * 保存用户微信信息
     *
     * @param userInfoReq
     * @return
     */
    @ApiOperation(value = "保存用户微信信息", notes = "保存用户微信信息", httpMethod = "POST")
    @ApiImplicitParam(name = "userInfoReq", value = "用户详细实体user", required = true, dataType = "SetupWechatInfoReq")
    @RequestMapping(value = "setupWechatInfo", method = {RequestMethod.POST})
    @UserValidate
    public Response<Boolean> setupUserWechatInfo(@RequestBody SetupWechatInfoReq userInfoReq) {
        userInfoService.updateUserWechatInfo(userInfoReq);
        return Response.success(Boolean.TRUE);
    }

    /**
     * 保存用户相关信息及设置预产期
     *
     * @param setupPregancyInfoReq
     * @return
     */
    @ApiOperation(value = "保存用户相关信息及设置预产期", notes = "保存用户相关信息及设置预产期", httpMethod = "POST")
    @ApiImplicitParam(name = "userInfoReq", value = "用户详细实体user", required = true, dataType = "SetupPregancyInfoReq")
    @RequestMapping(value = "setupPregancyInfo", method = {RequestMethod.POST})
    @UserValidate
    public Response<Boolean> setupPregancyInfo(@RequestBody SetupPregancyInfoReq setupPregancyInfoReq) {
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
    @ApiImplicitParam(name = "userInfoReq", value = "用户详细实体user", required = true, dataType = "SetupLactationInfoReq")
    @RequestMapping(value = "setupBabyInfo", method = {RequestMethod.POST})
    @UserValidate
    public Response<Boolean> setupBabyInfo(@RequestBody SetupLactationInfoReq setupLactationInfoReq) {
        userInfoService.updateLactationInfo(setupLactationInfoReq);
        babyInfoService.updateBabyInfo(setupLactationInfoReq.getBabyInfoList());
        lactationTodoService.initLactationTodo(HtmlUtil.getUserId());
        return Response.success(Boolean.TRUE);
    }

}
