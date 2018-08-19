package com.momassistant.entity.wechat;

import lombok.Data;

import java.util.Map;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Data
public class WechatSendMsgReq {
    private String touser;
    private String template_id;
    private Miniprogram miniprogram;
    private Map<String, MsgItem> data;
}
