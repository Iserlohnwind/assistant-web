package com.momassistant.wechat;

import java.io.Serializable;

/**
 * Created by zhufeng on 2018/8/24.
 */
public class WeiXinSendValue implements Serializable{

    private String value;
    private String color;
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

}