package com.momassistant.entity.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhufeng on 2018/8/19.
 */
@Data
public class SetupWechatInfoReq implements Serializable{

    private String wechatName;
    private String userHeadPic;
}
