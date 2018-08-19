package com.momassistant.entity;

import lombok.Data;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Data
public class WechatGetAccessTokenResponse {
    private String access_token;
    private Integer expires_in;
    private Integer errcode;
    private String errmsg;
}
