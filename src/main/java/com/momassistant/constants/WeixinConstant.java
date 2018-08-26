package com.momassistant.constants;

import com.momassistant.enums.WchatAppType;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class WeixinConstant {
    public static final String LITEAPP_APP_ID = "wxcf5296580df8afa2";
    public static final String LITEAPP_SECRET_KEY = "95d96e5b40a3a335ae69960be1a079f9";

    public static final String PUBLIC_ACCOUNT_APP_ID = "wx547df2035c15a16d";
    public static final String PUBLIC_ACCOUNT_SECRET_KEY = "f1554b6df05560e583fc182ae085f894";

    public static final String SMALL_APP_AUTH_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=" + LITEAPP_APP_ID + "&secret=" + LITEAPP_SECRET_KEY + "&js_code=%s&grant_type=authorization_code";

    public static final String PUBLIC_ACCOUNT_AUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + PUBLIC_ACCOUNT_APP_ID + "&secret=" + PUBLIC_ACCOUNT_SECRET_KEY + "&code=%s&grant_type=authorization_code";
    public static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + PUBLIC_ACCOUNT_APP_ID + "&secret=" + PUBLIC_ACCOUNT_SECRET_KEY;

    public static final String SEND_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";



    public static String getAuthUrl(WchatAppType wchatAppType, String code) {
        String url;
        if (wchatAppType == WchatAppType.SMALL_PROGRAM) {
            url = String.format(WeixinConstant.SMALL_APP_AUTH_URL, code);
        } else {
            url = String.format(WeixinConstant.PUBLIC_ACCOUNT_AUTH_URL, code);
        }
        return url;
    }
}
