package com.momassistant;

/**
 * Created by zhufeng on 2018/8/14.
 */
public enum  ReturnCode {
    OK(200, "正常"),
    WEIXIN_AUTH_FAILED(500, "微信认证失败"),
    TOKEN_EXPIRED(401, "token已经过期"),
    UKNOWN(500, "未知错误"),

    USER_TYPE_NO_CORRECT(10001, "用户类型不符"),
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
