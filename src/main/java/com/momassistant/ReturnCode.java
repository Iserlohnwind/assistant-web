package com.momassistant;

/**
 * Created by zhufeng on 2018/8/14.
 */
public enum  ReturnCode {
    OK(200),
    LOGIN_FAILED(500),
    ;
    private int code;
    ReturnCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
