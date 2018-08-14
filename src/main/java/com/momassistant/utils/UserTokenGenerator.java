package com.momassistant.utils;

import org.springframework.util.DigestUtils;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class UserTokenGenerator {
    public static String createToken(int userId, String openId) {
        return DigestUtils.md5DigestAsHex((userId + openId + System.currentTimeMillis()).getBytes());
    }
}
