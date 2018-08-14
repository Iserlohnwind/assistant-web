package com.momassistant;

/**
 * Created by zhufeng on 2018/8/14.
 */
public enum  ReturnCode {
    OK(200, "正常"),
    LOGIN_FAILED(500, ""),
    TOKEN_EXPIRED(500, "token已经过期"),
    UKNOWN(500, "未知错误"),

    ;
    private int code;
    private String msg;
    ReturnCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
