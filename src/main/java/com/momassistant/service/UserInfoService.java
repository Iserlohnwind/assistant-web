package com.momassistant.service;

import com.momassistant.entity.request.SetupLactationInfoReq;
import com.momassistant.entity.request.SetupPregancyInfoReq;
import com.momassistant.entity.request.SetupWechatInfoReq;
import com.momassistant.mapper.UserInfoMapper;
import com.momassistant.mapper.UserSessionMapper;
import com.momassistant.mapper.model.UserInfo;
import com.momassistant.mapper.model.UserSession;
import com.momassistant.utils.HtmlUtil;
import com.momassistant.utils.WrapUtils;
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

    public void updateUserWechatInfo(SetupWechatInfoReq userInfoReq) {
        userInfoMapper.updateUserWechat(
                HtmlUtil.getUserId(),
                userInfoReq.getWechatName(),
                userInfoReq.getUserHeadPic()
        );
    }

    public boolean updatePregancyInfo(SetupPregancyInfoReq userInfoReq) {
        int userType = WrapUtils.unwrap(userInfoMapper.getUserType(HtmlUtil.getUserId()), 0);
        if (userType == 2) {
            return false;
        }
        userInfoMapper.updatePregancyInfo(
                HtmlUtil.getUserId(),
                userInfoReq.getUserName(),
                userInfoReq.getMobile(),
                userInfoReq.getUserRegion(),
                userInfoReq.getEdc()
        );
        return true;
    }

    public boolean updateLactationInfo(SetupLactationInfoReq setupLactationInfoReq) {
        int userType = WrapUtils.unwrap(userInfoMapper.getUserType(HtmlUtil.getUserId()), 0);
        if (userType == 1) {
            return false;
        }
        userInfoMapper.updateLactationInfo(
                HtmlUtil.getUserId(),
                setupLactationInfoReq.getUserName(),
                setupLactationInfoReq.getMobile(),
                setupLactationInfoReq.getUserRegion()
        );
        return true;
    }

    public void bindPublicAccount(int userId, String publicAccountOpenId) {
        userInfoMapper.updatePublicAccountOpenIdByUserId(
                userId, publicAccountOpenId
        );
    }
}
