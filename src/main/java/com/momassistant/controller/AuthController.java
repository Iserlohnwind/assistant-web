package com.momassistant.controller;

import com.momassistant.ReturnCode;
import com.momassistant.constants.Constant;
import com.momassistant.service.UserInfoService;
import com.momassistant.entity.Response;
import com.momassistant.entity.UserSession;
import com.momassistant.mapper.model.UserInfo;
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
    @RequestMapping("login")
    public Response<UserSession> login(String code) {
        String openId = WechatAuthUtil.auth(code);
        if (StringUtils.isNotEmpty(openId)) {
            return Response.error(ReturnCode.LOGIN_FAILED);
        }
        UserInfo userInfo = userInfoService.getUser(openId);
        if (userInfo == null) {
            return Response.error(ReturnCode.LOGIN_FAILED);
        }

        String token = UserTokenGenerator.createToken(userInfo.getUserId(), openId);
        //会话要缓存在服务器一段时间
        userInfoService.updateToken(userInfo.getUserId(), token, DateUtil.addSeconds(new Date(), Constant.USER_SESSION_TTL));


        UserSession userSession = new UserSession();
        userSession.setUserId(userInfo.getUserId());
        userSession.setUserToken(token);
        userSession.setTtl(Constant.USER_SESSION_TTL);
        return Response.success(userSession);
    }

    @RequestMapping("testAuth")
    public Response testAuth() {
        return Response.success(null);
    }

}
