package com.momassistant.wechat;

import java.io.Serializable;

/**
 * Created by chuxiaobao on 18/3/9.
 */
public class Miniprogram implements Serializable{
    private static final long serialVersionUID = 1120465005087102126L;

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
