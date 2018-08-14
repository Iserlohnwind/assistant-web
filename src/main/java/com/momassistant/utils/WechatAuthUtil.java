package com.momassistant.utils;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.constants.WeixinConstant;
import com.momassistant.entity.Response;
import com.momassistant.entity.UserSession;
import com.momassistant.entity.WechatAuthResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class WechatAuthUtil {
    public static String auth(String code) {
        //微信的接口
        String url = String.format(WeixinConstant.AUTH_URL, code);
        RestTemplate restTemplate = new RestTemplate();
        //进行网络请求,访问url接口
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        //根据返回值进行后续操作
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String sessionData = responseEntity.getBody();

            //解析从微信服务器获得的openid和session_key;
            WechatAuthResponse weixinAuthResponse = JSONObject.parseObject(sessionData, WechatAuthResponse.class);
            if (weixinAuthResponse.getErrcode() == null) {
                return weixinAuthResponse.getOpenid();
            }
        }
        return null;
    }
}
