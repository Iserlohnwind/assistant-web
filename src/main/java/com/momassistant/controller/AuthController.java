package com.momassistant.controller;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.ReturnCode;
import com.momassistant.annotations.UserValidate;
import com.momassistant.constants.Constant;
import com.momassistant.constants.WeixinConstant;
import com.momassistant.entity.request.LoginReq;
import com.momassistant.entity.response.LoginResp;
import com.momassistant.enums.WchatAppType;
import com.momassistant.service.UserInfoService;
import com.momassistant.entity.Response;
import com.momassistant.utils.DateUtil;
import com.momassistant.utils.HtmlUtil;
import com.momassistant.utils.UserTokenGenerator;
import com.momassistant.wechat.WeixinSmsService;
import com.momassistant.wechat.WexinAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Api("登录认证api")
@RestController
public class AuthController {
    protected static final Log logger = LogFactory.getLog(AuthController.class);

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WexinAuthService wexinAuthService;
    @Autowired
    private WeixinSmsService weixinSmsService;

    //获取凭证校检接口
    @ApiOperation(value = "获取凭证校检接口", notes = "获取凭证校检接口", httpMethod = "POST")
    @ApiImplicitParam(name = "code", value = "微信api返回的code", required = true, dataType = "String")
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public Response<LoginResp> login(LoginReq loginReq) {
        String openId = wexinAuthService.auth(loginReq.getCode(), WchatAppType.SMALL_PROGRAM);
        if (StringUtils.isEmpty(openId)) {
            return Response.error(ReturnCode.WEIXIN_AUTH_FAILED);
        }
        int userId = userInfoService.getUserId(openId);
        if (userId <= 0) {
            return Response.error(ReturnCode.WEIXIN_AUTH_FAILED);
        }

        String token = UserTokenGenerator.createToken(userId, openId);
        //会话要缓存在服务器一段时间
        userInfoService.updateToken(userId, token, DateUtil.addSeconds(new Date(), Constant.USER_SESSION_TTL));


        LoginResp loginResp = new LoginResp();
        loginResp.setUserId(userId);
        loginResp.setUserToken(token);
        loginResp.setTtl(Constant.USER_SESSION_TTL);
        loginResp.setPublicAcccountAppId(WeixinConstant.PUBLIC_ACCOUNT_APP_ID);
        return Response.success(loginResp);
    }

    //获取凭证校检接口
    @ApiOperation(value = "公众号用户信息和小程序用户信息绑定", notes = "公众号用户信息和小程序用户信息绑定", httpMethod = "POST")
    @ApiImplicitParam(name = "code", value = "微信api返回的code", required = true, dataType = "String")
    @RequestMapping(value = "/auth/bind", method = RequestMethod.POST)
    @UserValidate
    public Response<Boolean> bind(LoginReq loginReq) {
        logger.info("bind loginReq:" + JSONObject.toJSONString(loginReq));
        String openId = wexinAuthService.auth(loginReq.getCode(), WchatAppType.PUBLIC_ACCOUNT);
        if (StringUtils.isEmpty(openId)) {
            return Response.success(Boolean.FALSE);
        }
        userInfoService.bindPublicAccount(HtmlUtil.getUserId(), openId);
        return Response.success(Boolean.TRUE);
    }
    @RequestMapping(value = "/auth/sendSms", method = RequestMethod.POST)
    @UserValidate
    public Response<Boolean> sendSms(String phoneNo) {
        return Response.success(weixinSmsService.sendMessage(phoneNo));
    }

}
