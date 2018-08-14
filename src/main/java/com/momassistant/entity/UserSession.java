package com.momassistant.entity;

import lombok.Data;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Data
public class UserSession {
    private int userId;
    private String userToken;
}
