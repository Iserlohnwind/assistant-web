package com.momassistant.wechat;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.cache.CacheManager;
import com.momassistant.constants.WeixinConstant;
import com.momassistant.utils.HttpClientUtils;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by zhufeng on 2018/8/24.
 */
@Service
public class WeiXinTokenService {
    private static final String ACCESS_TOKEN_CACHE_KEY = "accessToken";

    public String getAccessToken() {
        String accessToken = CacheManager.getData(ACCESS_TOKEN_CACHE_KEY);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=");
        urlBuffer.append(WeixinConstant.PUBLIC_ACCOUNT_APP_ID);
        urlBuffer.append("&secret=");
        urlBuffer.append(WeixinConstant.PUBLIC_ACCOUNT_SECRET_KEY);
        GetMethod getMethod = new GetMethod(urlBuffer.toString());
        String response = HttpClientUtils.executeGetMethod(getMethod);
        if (StringUtils.isNotEmpty(response)) {
            JSONObject responseJson = JSONObject.parseObject(response);
            if (responseJson.containsKey("access_token")) {
                accessToken = responseJson.getString("access_token");
                CacheManager.setData(ACCESS_TOKEN_CACHE_KEY, accessToken, 3600);
                return accessToken;
            }
        }
        return accessToken;
    }
}
