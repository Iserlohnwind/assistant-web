package com.momassistant.constants;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class WeixinConstant {
    public static final String APP_ID = "";
    public static final String SECRET_KEY = "";
    public static final String AUTH_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APP_ID + "&secret=" + SECRET_KEY + "&js_code=%s&grant_type=authorization_code";;
}
