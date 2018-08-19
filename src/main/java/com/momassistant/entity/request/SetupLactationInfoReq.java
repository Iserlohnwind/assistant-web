package com.momassistant.entity.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/19.
 */
@Data
public class SetupLactationInfoReq implements Serializable {
    private String userName;
    private String userRegion;
    private int gender;
    private String mobile;
    private int userType;

    private List<BabyInfoReq> babyInfoList;

}
