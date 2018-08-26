package com.momassistant.wechat;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.utils.HttpClientUtils;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhufeng on 2018/8/24.
 */
@Service
public class WeiXinMessageService {
    @Autowired
    private WeiXinTokenService weiXinTokenService;
    public void sendTemplateMessage(WeiXinTemplate weiXinTemplate) {
        String accessToken = weiXinTokenService.getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            return;
        }
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=");
        urlBuffer.append(accessToken);
        PostMethod postMethod = new PostMethod(urlBuffer.toString());
        HttpClientUtils.executePostMethod(postMethod, JSONObject.toJSONString(weiXinTemplate));
    }
}
