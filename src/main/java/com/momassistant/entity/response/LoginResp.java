package com.momassistant.entity.response;

import lombok.Data;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Data
public class LoginResp {
    private int userId;
    private String userToken;
    private long ttl;
}
