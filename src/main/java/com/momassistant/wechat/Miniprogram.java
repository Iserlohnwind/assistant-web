package com.momassistant.wechat;

import java.io.Serializable;

public class Miniprogram implements Serializable{

    private static final long serialVersionUID = -2900517249355663474L;
    private String pagepath;
    private String appid;

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
