package com.momassistant.controller;

import com.momassistant.ReturnCode;
import com.momassistant.annotations.UserValidate;
import com.momassistant.constants.Constant;
import com.momassistant.constants.WeixinConstant;
import com.momassistant.entity.Response;
import com.momassistant.entity.request.LoginReq;
import com.momassistant.entity.response.LoginResp;
import com.momassistant.enums.WchatAppType;
import com.momassistant.service.UserInfoService;
import com.momassistant.utils.DateUtil;
import com.momassistant.utils.HtmlUtil;
import com.momassistant.utils.UserTokenGenerator;
import com.momassistant.wechat.WexinAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Controller
public class IndexController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private WexinAuthService wexinAuthService;
    //获取凭证校检接口
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }


    @RequestMapping(value = "/UravozFnlm.txt")
    public String txt() {
        return "txt";
    }
}
