package com.momassistant.wechat;

import java.io.Serializable;


public class WeiXinSendValue implements Serializable{
    private static final long serialVersionUID = 3228807272076076691L;

    public WeiXinSendValue(String value, String color) {
        this.value = value;
        this.color = color;
    }

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