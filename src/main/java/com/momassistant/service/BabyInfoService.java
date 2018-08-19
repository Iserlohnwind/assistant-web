package com.momassistant.service;

import com.momassistant.entity.request.BabyInfoReq;
import com.momassistant.entity.request.UserInfoReq;
import com.momassistant.mapper.BabyInfoMapper;
import com.momassistant.utils.HtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BabyInfoService {
    @Autowired
    private BabyInfoMapper babyInfoMapper;

    public void updateBabyInfo(List<BabyInfoReq> babyInfoReqList) {
        for (BabyInfoReq babyInfoReq : babyInfoReqList) {
            babyInfoMapper.insertBabyInfo(HtmlUtil.getUserId(),
                    babyInfoReq.getBabyName(),
                    babyInfoReq.getBabyGender(),
                    babyInfoReq.getBabyBirthday()
            );
        }
    }


}
