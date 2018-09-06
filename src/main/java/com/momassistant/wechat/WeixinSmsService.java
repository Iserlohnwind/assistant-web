package com.momassistant.wechat;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.momassistant.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by zhufeng on 2018/9/5.
 */
@Service
public class WeixinSmsService {
    public boolean sendMessage(String phoneNo) {
        // 短信应用SDK AppID
        int appid = 1400132671; // 1400开头

// 短信应用SDK AppKey
        String appkey = "aa13314f8f173855e92a542c5c93d220";

// 短信模板ID，需要在短信应用中申请
        int templateId = 181306; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
//templateId7839对应的内容是"您的验证码是: {1}"
// 签名
        String smsSign = "未来色传媒科技"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
        String randomCode = randCode();

        try {
            String[] params = {randomCode, "5"};//数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNo,
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result);
        } catch (Exception e) {
            // HTTP响应码错误
            e.printStackTrace();
            return false;
        }

        CacheManager.setData("phoneNo:" + phoneNo, randomCode, 300);
        return true;
    }

    public boolean verify(String phoneNo, String verifyCode) {
        String cacheCode = CacheManager.getData("phoneNo:" + phoneNo);
        if (cacheCode != null && cacheCode.equals(verifyCode)) {
            CacheManager.clear("phoneNo:" + phoneNo);
            return true;
        }
        return false;
    }

    private String randCode() {
        StringBuilder codeSb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i <= 3; i++) {
            codeSb.append(random.nextInt(10));
        }
        return codeSb.toString();
    }
}
