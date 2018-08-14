package com.momassistant.service;

import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.UserSessionMapper;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.mapper.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Component
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserSessionMapper userSessionMapper;

    public UserInfo getUser(String openId) {
        UserInfo userInfo = userInfoMapper.getUser(openId);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setOpenId(openId);
            try {
                userInfoMapper.createUser(userInfo);
            } catch (DuplicateKeyException ignore) {
                userInfo = userInfoMapper.getUser(openId);
            }
        }
        return userInfo;
    }

    public void updateToken(int userId, String userToken, Date expiredAt) {
        try {
            userSessionMapper.insertToken(userId, userToken, expiredAt);
        } catch (DuplicateKeyException ignore) {
            userSessionMapper.updateToken(userId, userToken, expiredAt);
        }
    }

    public boolean validateToken(int userId, String userToken) {
        UserSession userSession = userSessionMapper.findByUserIdAndToken(userId, userToken);
        if (userSession != null) {
            return true;
        }
        return false;
    }
}
