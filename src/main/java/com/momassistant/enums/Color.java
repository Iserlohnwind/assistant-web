package com.momassistant.enums;

/**
 * Created by zhufeng on 2018/8/30.
 */
public enum  Color {
    BLACK("#000000"),;

    private String code;
    Color(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
