package com.momassistant.wechat;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.constants.WeixinConstant;
import com.momassistant.enums.WchatAppType;
import com.momassistant.utils.HttpClientUtils;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Service
public class WexinAuthService {
    protected static final Log logger = LogFactory.getLog(WexinAuthService.class);

    public static String auth(String code, WchatAppType wchatAppType) {
        //微信的接口
        String url = WeixinConstant.getAuthUrl(wchatAppType, code);
        GetMethod getMethod = new GetMethod(url);
        String respJson = HttpClientUtils.executeGetMethod(getMethod);
        if(StringUtils.isNotEmpty(respJson))
        {
            logger.info(respJson);
            //解析从微信服务器获得的openid和session_key;
            JSONObject resp = JSONObject.parseObject(respJson);
            if (resp.containsKey("errcode")) {
                return resp.getString("openid");
            }
        }
        return null;
    }
}
