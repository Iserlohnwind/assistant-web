package com.momassistant.entity;

import lombok.Data;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Data
public class WechatAuthResponse {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode;
    private String errmsg;
}
