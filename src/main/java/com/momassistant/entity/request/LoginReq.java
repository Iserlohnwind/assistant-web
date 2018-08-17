package com.momassistant.entity.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhufeng on 2018/8/17.
 */
@Data
public class LoginReq implements Serializable {
    private String code;
}
