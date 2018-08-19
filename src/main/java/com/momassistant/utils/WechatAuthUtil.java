package com.momassistant.utils;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.constants.WeixinConstant;
import com.momassistant.entity.WechatAuthResponse;
import com.momassistant.entity.WechatGetAccessTokenResponse;
import com.momassistant.entity.response.TodoDetailItem;
import com.momassistant.entity.wechat.MsgItem;
import com.momassistant.entity.wechat.WechatSendMsgReq;
import com.momassistant.enums.TodoMainType;
import com.momassistant.enums.WechatMsgTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class WechatAuthUtil {
    protected static final Log logger = LogFactory.getLog(WechatAuthUtil.class);

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
            logger.info(sessionData);
            //解析从微信服务器获得的openid和session_key;
            WechatAuthResponse weixinAuthResponse = JSONObject.parseObject(sessionData, WechatAuthResponse.class);
            if (weixinAuthResponse.getErrcode() == null) {
                return weixinAuthResponse.getOpenid();
            }
        }
        return null;
    }


    public static String getAccessToken() {
        String url = WeixinConstant.GET_ACCESS_TOKEN_URL;
        RestTemplate restTemplate = new RestTemplate();
        //进行网络请求,访问url接口
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        //根据返回值进行后续操作
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String sessionData = responseEntity.getBody();

            //解析从微信服务器获得的openid和session_key;
            WechatGetAccessTokenResponse wechatGetAccessTokenResponse = JSONObject.parseObject(sessionData, WechatGetAccessTokenResponse.class);
            if (wechatGetAccessTokenResponse.getErrcode() == null) {
                return wechatGetAccessTokenResponse.getAccess_token();
            }
        }
        return null;
    }

    public static void sendMsg(TodoMainType todoMainType, String openId, Map<String, String> originData) {
        WechatMsgTemplate wechatMsgTemplate = null;
        if (todoMainType == TodoMainType.GESTATION) {
            wechatMsgTemplate = WechatMsgTemplate.GESTATION_MSG;
        } else {
            wechatMsgTemplate = WechatMsgTemplate.LACTATION_MSG;
        }

        String[] keywords = wechatMsgTemplate.getKeywords();
        List<String> keywordList = Stream.of(keywords).collect(Collectors.toList());

        String accessToken = getAccessToken();
        String url = String.format(WeixinConstant.SEND_MSG_URL, accessToken);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        WechatSendMsgReq wechatSendMsgReq = new WechatSendMsgReq();
        wechatSendMsgReq.setTouser(openId);
        wechatSendMsgReq.setTemplate_id(wechatMsgTemplate.getTemplateId());
        Map<String, MsgItem> data = new HashMap<>();
        for (String key : originData.keySet()) {
            MsgItem msgItem = new MsgItem();
            msgItem.setValue(originData.get(key));
            msgItem.setColor("#888888");
            data.put(key, msgItem);
        }
        wechatSendMsgReq.setData(data);

        HttpEntity<String> entity = new HttpEntity<String>(JSONObject.toJSONString(wechatSendMsgReq),headers);
        //进行网络请求,访问url接口
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

}
