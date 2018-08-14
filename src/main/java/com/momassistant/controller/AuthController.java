package com.momassistant.controller;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.ReturnCode;
import com.momassistant.constants.WeixinConstant;
import com.momassistant.entity.Response;
import com.momassistant.entity.UserSession;
import com.momassistant.entity.WechatAuthResponse;
import com.momassistant.utils.UserTokenGenerator;
import com.momassistant.utils.WechatAuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class AuthController {

    //获取凭证校检接口
    public Response<UserSession> login(String code)
    {
        String openId = WechatAuthUtil.auth(code);
        if (StringUtils.isNotEmpty(openId)) {
            return Response.error(ReturnCode.LOGIN_FAILED);
        }
        int userId = 0;
        //根据openid查询或者创建新用户
        //.....
        String token = UserTokenGenerator.createToken(userId, openId);

        UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        userSession.setUserToken(token);
        return Response.success(userSession);
    }
}
