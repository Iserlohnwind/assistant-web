package com.momassistant.controller;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.ReturnCode;
import com.momassistant.annotations.UserValidate;
import com.momassistant.constants.Constant;
import com.momassistant.constants.WeixinConstant;
import com.momassistant.entity.Response;
import com.momassistant.entity.request.LoginReq;
import com.momassistant.entity.response.LoginResp;
import com.momassistant.enums.WchatAppType;
import com.momassistant.enums.WechatMsgTemplate;
import com.momassistant.mapper.model.TodoType;
import com.momassistant.mapper.model.TodoTypeDetail;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.service.UserInfoService;
import com.momassistant.utils.DateUtil;
import com.momassistant.utils.HtmlUtil;
import com.momassistant.utils.HttpClientUtils;
import com.momassistant.utils.UserTokenGenerator;
import com.momassistant.wechat.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Controller
public class IndexController {
@Autowired
private WeiXinMessageService weiXinMessageService;
    @Autowired
    private WeiXinTokenService weiXinTokenService;
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
    @RequestMapping(value = "/sendMessage")
    @ResponseBody
    public String sendMessage() {
            String accessToken = weiXinTokenService.getAccessToken();
            if (StringUtils.isEmpty(accessToken)) {
                return "token is null";
            }
            WeiXinTemplate weiXinTemplate = buildWeixinTemplate();
            StringBuffer urlBuffer = new StringBuffer();
            urlBuffer.append("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=");
            urlBuffer.append(accessToken);
            PostMethod postMethod = new PostMethod(urlBuffer.toString());
            return HttpClientUtils.executePostMethod(postMethod, JSONObject.toJSONString(weiXinTemplate));
    }
    private static final String FIRST_TEMPLATE = "亲爱的准妈妈,%s天后您将进行%s";

    private WeiXinTemplate buildWeixinTemplate() {
        WeiXinTemplate<GestationMessageData> weiXinTemplate = new WeiXinTemplate<GestationMessageData>();
        weiXinTemplate.setTemplate_id(WechatMsgTemplate.GESTATION_MSG.getTemplateId());
        weiXinTemplate.setTouser("odIy100yyiMRNPp5XqofA5x3GsFM");
        GestationMessageData gestationMessageData = new GestationMessageData();
        gestationMessageData.setFirst(new WeiXinSendValue(String.format(FIRST_TEMPLATE, "1", "xxx"), "#888888"));
        gestationMessageData.setKeyword1(new WeiXinSendValue(DateUtil.format(new Date()), "#888888"));
        gestationMessageData.setRemark(new WeiXinSendValue("点击查看本次产检更多注意事项吧~", "#888888"));
        gestationMessageData.setKeyword2(new WeiXinSendValue("blabla~~~", "#888888"));
        weiXinTemplate.setData(gestationMessageData);
        return weiXinTemplate;
    }

}
