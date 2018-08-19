package com.momassistant.service;

import com.momassistant.entity.request.UserInfoReq;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.UserSessionMapper;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.mapper.model.UserSession;
import org.springframework.beans.BeanUtils;
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

    public UserInfo updateUserInfo(UserInfoReq userInfoReq) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoReq, userInfo);
        userInfoMapper.updateUserDetail(userInfo);
        return userInfoMapper.getUserDetail(userInfoReq.getUserId());
    }

    public int getUserId(String openId) {
        Integer userId = userInfoMapper.getUserId(openId);
        if (userId == null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setOpenId(openId);
            try {
                userInfoMapper.createUser(userInfo);
                userId = userInfo.getUserId();
            } catch (DuplicateKeyException ignore) {
                userId = userInfoMapper.getUserId(openId);
            }
        }
        return userId != null ? userId : 0;
    }

    public UserInfo getUserDetail(int userId) {
        return userInfoMapper.getUserDetail(userId);
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

    public void updateUserWechatInfo(UserInfoReq userInfoReq) {
        userInfoMapper.updateUserWechat(
                userInfoReq.getUserId(),
                userInfoReq.getWechatName(),
                userInfoReq.getUserHeadPic()
        );
    }

    public void updatePregancyInfo(UserInfoReq userInfoReq) {
        userInfoMapper.updatePregancyInfo(
                userInfoReq.getUserId(),
                userInfoReq.getUserName(),
                2,
                userInfoReq.getMobile(),
                userInfoReq.getUserRegion(),
                userInfoReq.getEdc()
        );
    }
}
