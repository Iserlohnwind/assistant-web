package com.momassistant.controller;

import com.momassistant.ReturnCode;
import com.momassistant.constants.Constant;
import com.momassistant.entity.request.LoginReq;
import com.momassistant.entity.response.LoginResp;
import com.momassistant.service.UserInfoService;
import com.momassistant.entity.Response;
import com.momassistant.utils.DateUtil;
import com.momassistant.utils.UserTokenGenerator;
import com.momassistant.utils.WechatAuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@RestController
public class AuthController {

    @Autowired
    private UserInfoService userInfoService;
    //获取凭证校检接口
    @RequestMapping("/auth/login")
    public Response<LoginResp> login(LoginReq loginReq) {
        String openId = WechatAuthUtil.auth(loginReq.getCode());
        if (StringUtils.isNotEmpty(openId)) {
            return Response.error(ReturnCode.LOGIN_FAILED);
        }
        int userId = userInfoService.getUserId(openId);
        if (userId <= 0) {
            return Response.error(ReturnCode.LOGIN_FAILED);
        }

        String token = UserTokenGenerator.createToken(userId, openId);
        //会话要缓存在服务器一段时间
        userInfoService.updateToken(userId, token, DateUtil.addSeconds(new Date(), Constant.USER_SESSION_TTL));


        LoginResp loginResp = new LoginResp();
        loginResp.setUserId(userId);
        loginResp.setUserToken(token);
        loginResp.setTtl(Constant.USER_SESSION_TTL);
        return Response.success(loginResp);
    }

    @RequestMapping("/auth/testAuth")
    public Response testAuth() {
        return Response.success(null);
    }

}
