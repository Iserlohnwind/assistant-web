package com.momassistant.enums;

/**
 * Created by zhufeng on 2018/8/18.
 */
public enum WechatIdType {
    UNIONID(1),LITEAPP_OPENID(2),PUBLICACCOUNT_OPENID(3);
    private int type;
    WechatIdType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
